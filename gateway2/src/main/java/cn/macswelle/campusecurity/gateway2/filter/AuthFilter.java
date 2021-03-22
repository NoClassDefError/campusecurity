package cn.macswelle.campusecurity.gateway2.filter;

import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
import cn.macswelle.campusecurity.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

  private String[] skipAuthUrls = {"/user/login"};

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    String url = exchange.getRequest().getURI().getPath();

    //跳过不需要验证的路径
    if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url)) {
      return chain.filter(exchange);
    }
    String token = exchange.getRequest().getHeaders().getFirst("access-token");
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.info("token: " + token);
    if (JwtUtil.verifyToken(token, "gehanchen")) {
      String user = JwtUtil.getUserInfo(token);
      logger.info(user);
      //向headers中放文件，记得build
      ServerHttpRequest request = exchange.getRequest().mutate().header("user", user).build();
      //将现在的request 变成 exchange对象
      return chain.filter(exchange.mutate().request(request).build());
    }
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
