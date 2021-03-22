package cn.macswelle.campusecurity.gateway2.filter;

import cn.macswelle.campusecurity.common.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    logger.info("token: "+token);
    if (token != null && JwtUtil.verifyToken(token, "gehanchen"))
      return chain.filter(exchange);
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    return exchange.getResponse().setComplete();
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
