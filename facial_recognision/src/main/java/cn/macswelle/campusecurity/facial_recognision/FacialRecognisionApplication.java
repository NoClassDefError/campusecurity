package cn.macswelle.campusecurity.facial_recognision;

import com.baidu.aip.face.AipFace;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration
@Data
@EnableEurekaClient
@ComponentScan({"cn.macswelle.campusecurity.facial_recognision.service",
  "cn.macswelle.campusecurity.facial_recognision.controller"})
@EntityScan("cn.macswelle.campusecurity.database.entities")
@EnableJpaRepositories("cn.macswelle.campusecurity.database.repositories")
@ConfigurationProperties(prefix = "campusecurity.face")
public class FacialRecognisionApplication {

  public static void main(String[] args) {
    SpringApplication.run(FacialRecognisionApplication.class, args);
  }


//  @Bean
//  public ConnectionFactory connectionFactory() {
//    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//    connectionFactory.setAddresses("127.0.0.1:5672");
//    connectionFactory.setUsername("guest");
//    connectionFactory.setPassword("guest");
//    return connectionFactory;
//  }

  @Bean
  public ObjectMapper getMapper() {
    return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  private String secretKey;
  private String apiKey;
  private String appId;
  private String groupIdList;

  @Bean
  public AipFace getAipFace() {
    AipFace client = new AipFace(appId, apiKey, secretKey);
    client.setConnectionTimeoutInMillis(2000);
    client.setSocketTimeoutInMillis(60000);
    return client;
  }

  @Bean
  public String getGroupIdList() {
    return groupIdList;
  }

  @Bean
  public Queue HelloQueue() {
    return new Queue("faceDto");
  }
}
