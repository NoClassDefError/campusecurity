package cn.macswelle.campusecurity.gateway2.filter;

import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class DeviceAuthFilter implements GatewayFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("access-token");
    if (token != null) {
      if (JwtUtil.verifyToken(token, "gehanchen")) {
        String user = JwtUtil.getUserInfo(token);
        try {
          LoginDto2 loginDto2 = new ObjectMapper().readValue(user, LoginDto2.class);
          if (loginDto2.getAuth().equals("管理员") || loginDto2.getAuth().equals("超级管理员"))
            return chain.filter(exchange);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
