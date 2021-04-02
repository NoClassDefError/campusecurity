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
import java.util.Arrays;

@Component
public class DeviceAuthFilter implements GatewayFilter, Ordered {

  private String[] skipAuthUrls = {"/user/login", "/user/signUp", "/user/send"};

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String url = exchange.getRequest().getURI().getPath();
    if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(url))
      return chain.filter(exchange);

    boolean isAdmin = false;
    if (url.contains("/admin")) isAdmin = true;
    String user = exchange.getRequest().getHeaders().getFirst("user");
    Logger logger = LoggerFactory.getLogger(this.getClass());
    logger.info("user: " + user);

    try {
      LoginDto2 loginDto2 = new ObjectMapper().readValue(user, LoginDto2.class);
      if (isAdmin && (loginDto2.getAuth() == 0 || loginDto2.getAuth() == 1))
        return chain.filter(exchange);
      if (!isAdmin) return chain.filter(exchange);
    } catch (IOException e) {
      e.printStackTrace();
    }
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
