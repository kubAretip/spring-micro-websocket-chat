package pl.kubaretip.authservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.kubaretip.authservice.config.JWTConfig;

import java.util.Date;
import java.util.stream.Collectors;

public class JWTUtils {

    public static final String AUTHORITIES_KEY = "roles";
    public static final String SUB_ID_KEY = "subId";

    private final long tokenValidityTimeInMilliseconds;
    private final Algorithm sign;
    private final JWTConfig jwtConfig;

    public JWTUtils(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.tokenValidityTimeInMilliseconds = jwtConfig.getExpiration() * 1000L;
        this.sign = Algorithm.HMAC512(jwtConfig.getSecret());
    }

    public String buildToken(Authentication authentication) {
        var user = (SecurityUserDetails) authentication.getPrincipal();
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

    public boolean isValidAuthorizationHeaderValue(String authHeaderValue) {
        return StringUtils.isNotBlank(authHeaderValue)
                && authHeaderValue.contains(jwtConfig.getTokenPrefix())
                && StringUtils.isNotBlank(authHeaderValue.replace(jwtConfig.getTokenPrefix(), ""));
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        var decodedJWT = JWT
                .require(sign)
                .build()
                .verify(token);

        if (decodedJWT != null) {
            var authorities = decodedJWT.getClaim(AUTHORITIES_KEY)
                    .asList(String.class)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            var username = decodedJWT.getSubject();
            var userId = decodedJWT.getClaim(SUB_ID_KEY).asLong();
            var securityUser = new SecurityUserDetails(userId, username, "", authorities);

            return new UsernamePasswordAuthenticationToken(securityUser, token, authorities);

        }
        return null;
    }


}
