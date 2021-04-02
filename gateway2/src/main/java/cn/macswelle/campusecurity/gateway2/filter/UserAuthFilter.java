package cn.macswelle.campusecurity.gateway2.filter;

import cn.macswelle.campusecurity.common.dto.responseDto.LoginDto2;
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
public class UserAuthFilter implements GatewayFilter, Ordered {

  private String[] skipAuthUrls = {"/user/login", "/user/signUp", "/user/send"};

  /**
   * 抛弃session，采用jwt方案鉴权
   * 此方法在UserService登录接口响应之后执行
   * 未登录即可访问：skipAuthUrls
   * 普通用户2：/user /device
   * 管理员1： /device/admin
   * 超级管理员0：/user/admin /device/admin
   */
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
      if (loginDto2.getAuth() == 0 && isAdmin)
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

