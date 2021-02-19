package pl.kubaretip.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.kubaretip.authutils.jwt.JWTConfig;
import pl.kubaretip.authutils.security.SecurityUserDetailsImpl;


import java.util.Date;
import java.util.stream.Collectors;

import static pl.kubaretip.authutils.jwt.JWTConstants.AUTHORITIES_KEY;
import static pl.kubaretip.authutils.jwt.JWTConstants.SUB_ID_KEY;

public class JWTBuilder {

    private final long tokenValidityTimeInMilliseconds;
    private final Algorithm sign;

    public JWTBuilder(JWTConfig jwtConfig) {
        this.tokenValidityTimeInMilliseconds = jwtConfig.getExpiration() * 1000L;
        this.sign = Algorithm.HMAC512(jwtConfig.getSecret());
    }

    public String buildToken(Authentication authentication) {
        var user = (SecurityUserDetailsImpl) authentication.getPrincipal();
        var authorities = user.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        var expiresAt = new Date(System.currentTimeMillis() + this.tokenValidityTimeInMilliseconds);

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withClaim(SUB_ID_KEY, user.getId())
                .withClaim(AUTHORITIES_KEY, authorities)
                .sign(sign);
    }


}
