package cn.macswelle.campusecurity.gateway.filter;

import cn.macswelle.campusecurity.common.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter {

    //gateway 不能利用httpServletResponse鉴权，因为它与spring MVC冲突
//    @Autowired
//    private UserApi userApi;

    /**
     * 抛弃session，采用jwt方案鉴权
     * 此方法在UserService登录接口响应之后执行
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("access-token");
        if (token != null) {
            //判断身份
//            JwtUtil.verifyToken(token,"gehanchen");
            String userId = JwtUtil.getUserInfo(token);
//            userApi.getAuth(userId);
            return chain.filter(exchange);
        } else {
            //尚未登录，退回主页
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}

