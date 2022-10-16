package ua.ponarin.trivia.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import ua.ponarin.trivia.gateway.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtQueryParameterAuthenticationFilter extends JwtAuthenticationFilter {
    private final JwtService jwtService;
    @Override
    public String getToken(ServerWebExchange exchange) {
        return exchange.getRequest().getQueryParams().getFirst(("token"));
    }

    @Override
    public JwtService getJwtService() {
        return jwtService;
    }
}
