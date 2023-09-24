package com.example.spring.boot.redis.lock.message.service;


import com.example.spring.boot.redis.lock.message.entity.Message;
import com.example.spring.boot.redis.lock.message.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageRepository messageRepository;

  private final RedisTemplate<String, Object> redisTemplate;

  private final RedissonClient redissonClient;


  @Transactional
  public Message save(String text, String tap, long sleep) {
    return getMessage(text, tap, sleep);
  }


  @Transactional
  public synchronized Message syncSave(String text, String tap, long sleep) {
    return getMessage(text, tap, sleep);
  }


  @Transactional
  public Message redisSave(String text, String tap, long sleep) {

    final RLock lock = redissonClient.getLock("message");
    Message message = null;

    try {
      if(!lock.tryLock(1, 3, TimeUnit.SECONDS))
        return message;

      message = getMessage(text, tap, sleep);
    } catch (InterruptedException e) {
      log.error("error", e);
    } finally {
      if(lock != null && lock.isLocked()) {
        lock.unlock();
      }
    }
    return message;
  }

  private Long getSequence() {

    Long sequence = (Long) redisTemplate.opsForValue().get("sequence");

    if (sequence == null) {
      sequence = 1L;
    }
    redisTemplate.opsForValue().set("sequence", sequence + 1);
    return sequence;
  }

  public Message getMessage(String text, String tap, long sleep) {
    log.info("start {} => {}", tap, text);

    try {
      Thread.sleep(sleep);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Message message = Message.builder()
        .messageId(getSequence())
        .text(text)
        .build();

    log.info("start {} => {}", tap, text);
    return messageRepository.save(message);
  }
}
