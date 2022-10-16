package ua.ponarin.trivia.gateway.filter;

import io.jsonwebtoken.JwtException;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import ua.ponarin.trivia.gateway.service.JwtService;

public abstract class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            try {
                getJwtService().validateToken(getToken(exchange));
                return chain.filter(exchange);
            } catch (JwtException exception) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public abstract String getToken(ServerWebExchange exchange);

    public abstract JwtService getJwtService();
}
