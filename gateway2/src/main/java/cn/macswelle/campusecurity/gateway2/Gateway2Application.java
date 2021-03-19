package cn.macswelle.campusecurity.gateway2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@EnableDiscoveryClient
@SpringBootApplication
@Configuration
@ComponentScan("cn.macswelle.campusecurity.gateway2.filter")
public class Gateway2Application {
  public static void main(String[] args) {
    SpringApplication.run(Gateway2Application.class);
  }

  @Bean
  public CorsWebFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    // cookie跨域
    config.setAllowCredentials(Boolean.TRUE);
    config.addAllowedMethod("*");
    config.addAllowedOrigin("*");
    config.addAllowedHeader("*");
    // 配置前端js允许访问的自定义响应头
    config.addExposedHeader("setToken");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
    source.registerCorsConfiguration("/**", config);
    return new CorsWebFilter(source);
  }
}
