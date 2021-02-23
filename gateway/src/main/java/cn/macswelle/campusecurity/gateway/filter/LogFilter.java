package cn.macswelle.campusecurity.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class LogFilter implements GlobalFilter, Ordered {
    /**
     * 过滤器业务逻辑
     * ServerWebExchange相当于请求与响应的上下文，RequestContext
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("收到请求：" + exchange.getRequest().getPath() + exchange.getRequest().getRemoteAddress());
        logger.info("access-token:" + exchange.getRequest().getQueryParams().getFirst("access-token"));
//        logger.info(exchange.getRequest());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
