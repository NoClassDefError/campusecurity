package cn.macswelle.campusecurity.nvrlistener;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@ComponentScan({"cn.macswelle.campusecurity.sdk.controller",
  "cn.macswelle.campusecurity.sdk.service",
  "cn.macswelle.campusecurity.sdk"})
@EnableFeignClients("cn.macswelle.campusecurity.feignapi.deviceManager")
public class NvrlistenerApplication {

  public static void main(String[] args) {
    SpringApplication.run(NvrlistenerApplication.class, args);
  }

  @Bean
  public ObjectMapper getMapper() {
    return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }
}
