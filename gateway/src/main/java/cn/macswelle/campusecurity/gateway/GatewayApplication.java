package cn.macswelle.campusecurity.gateway;

import cn.macswelle.campusecurity.common.dto.LocationDto2;
import cn.macswelle.campusecurity.feignapi.deviceManager.ManagerApi;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.ServletContext;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"cn.macswelle.campusecurity.feignapi.userservice",
        "cn.macswelle.campusecurity.feignapi.deviceManager"})
public class GatewayApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Autowired
    private ServletContext context;

    @Autowired
    private ManagerApi managerApi;

    @Override
    public void run(String... args) {
        List<LocationDto2> list = managerApi.getLocations();
        context.setAttribute("locations", list);
        LoggerFactory.getLogger(this.getClass()).info("Set locations to server context:" + list.size());
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
