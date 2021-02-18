package pl.kubaretip.zuulserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.kubaretip.authutils.JWTConfig;
import pl.kubaretip.authutils.JWTFilter;
import pl.kubaretip.zuulserver.security.CustomAuthenticationEntryPoint;
import pl.kubaretip.authutils.JWTUtils;

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
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .mvcMatchers(HttpMethod.POST, "/api/auth-service" + jwtConfig().getAuthEndpoint()).permitAll()
            .mvcMatchers(HttpMethod.POST,"/api/user-service/users/register").permitAll()
            .anyRequest()
            .authenticated()
        .and()
            .addFilterAfter(new JWTFilter(jwtUtils()), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
        .and()
            .httpBasic()
            .disable();

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

    @Bean
    public CustomAuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
