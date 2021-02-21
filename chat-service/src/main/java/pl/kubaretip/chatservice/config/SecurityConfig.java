package pl.kubaretip.chatservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.kubaretip.authutils.jwt.JWTConfig;
import pl.kubaretip.authutils.jwt.JWTFilter;
import pl.kubaretip.authutils.jwt.JWTUtils;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off

        http
            .cors()
        .and()
            .csrf()
            .disable()
            .httpBasic()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
        .and()
            .addFilterAfter(new JWTFilter(jwtUtils()), UsernamePasswordAuthenticationFilter.class);

        // @formatter:on
    }

    @Bean
    public JWTConfig jwtConfig() {
        return new JWTConfig();
    }

    @Bean
    public JWTUtils jwtUtils() {
        return new JWTUtils(jwtConfig());
    }


}
