package pl.kubaretip.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.kubaretip.authutils.JWTConfig;
import pl.kubaretip.authutils.JWTFilter;
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
                .httpBasic()
                .disable()
                .addFilterAfter(new JWTFilter(jwtUtils()), BasicAuthenticationFilter.class)
                .authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/users/register").permitAll()
                .anyRequest()
                .authenticated();

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
