package cn.macswelle.campusecurity.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"cn.macswelle.campusecurity.userservice.service",
  "cn.macswelle.campusecurity.userservice.controller"})
@EntityScan("cn.macswelle.campusecurity.database.entities")
@EnableJpaRepositories("cn.macswelle.campusecurity.database.repositories")
@Configuration
@EnableCaching
public class UserserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserserviceApplication.class, args);
  }

  @Bean
  public RedisTemplate<String, Integer> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Integer> redis = new RedisTemplate<>();
    redis.setConnectionFactory(redisConnectionFactory);
    redis.afterPropertiesSet();
    return redis;
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory factory) {
    // 更改值的序列化方式，否则在Redis可视化软件中会显示乱码。默认为JdkSerializationRedisSerializer，
    // 要序列化的对象必须实现Serializable接口，用jackson序列化则不用
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
      .fromSerializer(new GenericJackson2JsonRedisSerializer());
//        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
//                .fromSerializer(RedisSerializer.java());
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
      .defaultCacheConfig()
      .serializeValuesWith(pair)      // 设置序列化方式
      .entryTtl(Duration.ofHours(1)); // 设置过期时间

    return RedisCacheManager
      .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
      .cacheDefaults(defaultCacheConfig).build();
  }
}
