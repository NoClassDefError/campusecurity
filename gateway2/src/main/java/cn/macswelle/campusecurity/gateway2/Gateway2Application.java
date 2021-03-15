package cn.macswelle.campusecurity.gateway2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableDiscoveryClient
@SpringBootApplication
@Controller
@ComponentScan("cn.macswelle.campusecurity.gateway2.filter")
public class Gateway2Application {
    public static void main(String[] args) {
        SpringApplication.run(Gateway2Application.class);
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
