package cn.macswelle.campusecurity.devicemanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("cn.macswelle.campusecurity.feignapi.adapter")
@ComponentScan({"cn.macswelle.campusecurity.devicemanage.controller",
        "cn.macswelle.campusecurity.devicemanage.service"})
@EntityScan("cn.macswelle.campusecurity.database.entities")
@EnableJpaRepositories("cn.macswelle.campusecurity.database.repositories")
public class DevicemanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevicemanageApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
