package pl.kubaretip.authutils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

import static pl.kubaretip.authutils.JWTConstants.AUTHORITIES_KEY;
import static pl.kubaretip.authutils.JWTConstants.SUB_ID_KEY;

public class JWTUtils {

    private final Algorithm sign;
    private final JWTConfig jwtConfig;

    public JWTUtils(JWTConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.sign = Algorithm.HMAC512(jwtConfig.getSecret());
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

    public JWTConfig getJwtConfig() {
        return jwtConfig;
    }
}
