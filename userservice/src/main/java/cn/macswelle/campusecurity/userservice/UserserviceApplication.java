package cn.macswelle.campusecurity.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({"cn.macswelle.campusecurity.userservice.service",
        "cn.macswelle.campusecurity.userservice.controller"})
@EntityScan("cn.macswelle.campusecurity.database.entities")
@EnableJpaRepositories("cn.macswelle.campusecurity.database.repositories")
public class UserserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserserviceApplication.class, args);
    }

}
