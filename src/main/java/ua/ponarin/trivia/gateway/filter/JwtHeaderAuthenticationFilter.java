package ua.ponarin.trivia.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import ua.ponarin.trivia.gateway.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtHeaderAuthenticationFilter extends JwtAuthenticationFilter {
    private final JwtService jwtService;
    @Override
    public String getToken(ServerWebExchange exchange) {
        var tokenWitBearerPrefix = exchange.getRequest().getHeaders().get("Authorization").get(0);
        return tokenWitBearerPrefix.split(" ")[1];
    }

    @Override
    public JwtService getJwtService() {
        return jwtService;
    }
}
