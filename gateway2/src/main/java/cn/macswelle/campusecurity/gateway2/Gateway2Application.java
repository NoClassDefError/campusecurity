package cn.macswelle.campusecurity.gateway2;

import cn.macswelle.campusecurity.gateway2.filter.DeviceAuthFilter;
import cn.macswelle.campusecurity.gateway2.filter.UserAuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration
@ComponentScan("cn.macswelle.campusecurity.gateway2.filter")
public class Gateway2Application {
  public static void main(String[] args) {
    SpringApplication.run(Gateway2Application.class);
  }

  @Bean
  public RouteLocator userAdminRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes().route(r ->
      r.path("/user/admin/**")
        .uri("lb://userservice")
        .filters(new UserAuthFilter())
        .id("userAdminService"))
      .build();
  }

  @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
    return builder.routes().route(r ->
      r.path("/device/admin/**")
        .uri("lb://devicemanage")
        .filter(new DeviceAuthFilter())
        .id("deviceAdminService"))
      .build();
  }
}
