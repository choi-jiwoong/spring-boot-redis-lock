package com.example.spring.boot.redis.lock.message.repository;

import com.example.spring.boot.redis.lock.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
