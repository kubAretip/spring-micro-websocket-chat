package pl.kubaretip.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.kubaretip.authservice.security.AuthenticationFailureHandler;
import pl.kubaretip.authservice.security.AuthenticationSuccessHandler;
import pl.kubaretip.authservice.security.JWTAuthenticationFilter;
import pl.kubaretip.authservice.security.JWTUtils;

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
            .mvcMatchers(HttpMethod.POST, jwtConfig().getAuthEndpoint()).permitAll()
            .anyRequest().authenticated();

        // @formatter:on
    }

    @Bean
    public JWTAuthenticationFilter authenticationFilter() throws Exception {
        var filter = new JWTAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(jwtUtils()));
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler());
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setFilterProcessesUrl(jwtConfig().getAuthEndpoint());
        return filter;
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
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
