package cn.macswelle.campusecurity.facial_recognision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("cn.macswelle.campusecurity.feignapi.adapter")
@EntityScan("cn.macswelle.campusecurity.database.entities")
@EnableJpaRepositories("cn.macswelle.campusecurity.database.repositories")
public class FacialRecognisionApplication {

  public static void main(String[] args) {
    SpringApplication.run(FacialRecognisionApplication.class, args);
  }

}
