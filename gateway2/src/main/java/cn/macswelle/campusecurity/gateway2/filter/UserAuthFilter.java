package cn.macswelle.campusecurity.gateway2.filter;

import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
public class UserAuthFilter implements GatewayFilter, Ordered {


  /**
   * 抛弃session，采用jwt方案鉴权
   * 此方法在UserService登录接口响应之后执行
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String token = exchange.getRequest().getHeaders().getFirst("access-token");
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.info("token: "+token);
    if (token != null) {
      if (JwtUtil.verifyToken(token, "gehanchen")) {
        String user = JwtUtil.getUserInfo(token);
        logger.info(user);
        try {
          LoginDto2 loginDto2 = new ObjectMapper().readValue(user, LoginDto2.class);
          if (loginDto2.getAuth().equals("超级管理员"))
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

