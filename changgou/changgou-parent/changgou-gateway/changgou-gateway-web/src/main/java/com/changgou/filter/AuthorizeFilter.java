package com.changgou.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器 :用于鉴权(获取令牌 解析 判断)
 *
 * @version 1.0
 * @package com.changgou.filter *
 * @since 1.0
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    public static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        //2.获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        //3.判断 是否为登录的URL 如果是 放行
//        if(request.getURI().getPath().startsWith("/api/user/login")){
//            return chain.filter(exchange);
//        }
        String path = request.getURI().getPath();
        if (!URLFilter.hasAuthorize(path)){
            return chain.filter(exchange);
        }
        //4.判断 是否为登录的URL 如果不是权限校验
        //4.1 从头header中获取令牌数据
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //令牌是否在头文件中。false：令牌不在头文件中->将令牌封装到头文件中，再传递给其他微服务
        boolean hasToken = true;

        if(StringUtils.isEmpty(token)){
            hasToken = false;
            //4.2 从cookie中中获取令牌数据
            HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(first!=null){
                token=first.getValue();//就是令牌的数据
            }
        }

        if(StringUtils.isEmpty(token)){
            hasToken = false;
            //4.3 从请求参数中获取令牌数据
            token= request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        }

        if(StringUtils.isEmpty(token)){
            //4.4. 如果没有数据 结束.
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //5 解析令牌数据 ( 判断解析是否正确,正确 就放行 ,否则 结束)
        try {
//            Claims claims = JwtUtil.parseJWT(token);
            //将token封装到头文件中
            //判断是否有bearer， 前缀，如果有，则添加
            if (!token.startsWith("bearer ") && !token.startsWith("Bearer ")){
                token = "bearer " + token;
            }
            if (!hasToken){
                //将令牌数据添加到头文件
                request.mutate().header(AUTHORIZE_TOKEN,token);
            }

        } catch (Exception e) {
            e.printStackTrace();
            //解析失败
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
