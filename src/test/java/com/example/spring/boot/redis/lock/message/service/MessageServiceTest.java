package com.example.spring.boot.redis.lock.message.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;


@SpringBootTest
@Execution(ExecutionMode.CONCURRENT)
public class MessageServiceTest {


  @Autowired
  private  MessageService messageService;


  @Test
  @Rollback(false)
  void synchronizedSave1() {
    for(int i = 0; i < 10; i++)
      messageService.save("test" + System.currentTimeMillis(), "", 10);
  }

  @Test
  @Rollback(false)
  void synchronizedSave2() {
    for(int i = 0; i < 10; i++)
      messageService.save("test" + System.currentTimeMillis(), "-----------------------", 20);
  }
}