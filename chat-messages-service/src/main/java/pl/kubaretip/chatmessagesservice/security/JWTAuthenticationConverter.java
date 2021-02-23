package pl.kubaretip.chatmessagesservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pl.kubaretip.authutils.jwt.JWTUtils;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JWTAuthenticationConverter implements ServerAuthenticationConverter {

    private final JWTUtils jwtUtils;

    public JWTAuthenticationConverter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        return Mono.justOrEmpty(exchange.getRequest().getHeaders())
                .flatMap(headers -> Mono.justOrEmpty(headers.getFirst(HttpHeaders.AUTHORIZATION)))
                .flatMap(authHeaderValue -> {
                    if (jwtUtils.isValidAuthorizationHeaderValue(authHeaderValue)) {
                        var token = authHeaderValue.replace(jwtUtils.getJwtConfig().getTokenPrefix(), "");
                        log.debug("Token {}", token);
                        return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
                    } else
                        return Mono.empty();
                });
    }
}
