package cn.macswelle.campusecurity.nvrlistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients("cn.macswelle.campusecurity.feignapi.deviceManager")
public class NvrlistenerApplication {

  public static void main(String[] args) {
    SpringApplication.run(NvrlistenerApplication.class, args);
  }

}
