package com.example.spring.boot.redis.lock.config;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {


  private final RedisProperties redisProperties;

  private final String REDISSON_HOST_PREFIX = "redis://";

  @Bean
  public RedissonClient redissonClient() {
    RedissonClient redisson = null;
    Config config = new Config();
    config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort());
    return Redisson.create(config);
  }
}
