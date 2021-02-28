package cn.macswelle.campusecurity.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("cn.macswelle.campusecurity.feignapi.userservice")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    public AuthFilter authFilter() {
//        return new AuthFilter();
//    }
//
//    @Bean
//    public LogFilter logFilter(){
//        return new LogFilter();
//    }
}
