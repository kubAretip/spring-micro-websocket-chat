package pl.kubaretip.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import pl.kubaretip.authservice.security.AuthenticationFailureHandler;
import pl.kubaretip.authservice.security.AuthenticationSuccessHandler;
import pl.kubaretip.authservice.security.JWTAuthenticationFilter;
import pl.kubaretip.authservice.security.JWTBuilder;
import pl.kubaretip.authutils.jwt.JWTConfig;
import pl.kubaretip.authutils.jwt.JWTFilter;
import pl.kubaretip.authutils.jwt.JWTUtils;


@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProblemSupport problemSupport;
    private final ObjectMapper objectMapper;

    public SecurityConfig(SecurityProblemSupport problemSupport,
                          ObjectMapper objectMapper) {
        this.problemSupport = problemSupport;
        this.objectMapper = objectMapper;
    }

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
            .authorizeRequests()
            .mvcMatchers(HttpMethod.POST, jwtConfig().getAuthEndpoint()).permitAll()
            .mvcMatchers(HttpMethod.POST,"/users").permitAll()
            .mvcMatchers(HttpMethod.PATCH,"/users/activate").permitAll()
            .anyRequest().authenticated()
        .and()
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        .and()
            .addFilterAfter(new JWTFilter(jwtUtils()), UsernamePasswordAuthenticationFilter.class);

        // @formatter:on
    }

    @Bean
    public JWTAuthenticationFilter authenticationFilter() throws Exception {
        var filter = new JWTAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(jwtBuilder(), objectMapper));
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler(objectMapper));
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setFilterProcessesUrl(jwtConfig().getAuthEndpoint());
        return filter;
    }

    @Bean
    public JWTUtils jwtUtils() {
        return new JWTUtils(jwtConfig());
    }

    @Bean
    public JWTConfig jwtConfig() {
        return new JWTConfig();
    }

    @Bean
    public JWTBuilder jwtBuilder() {
        return new JWTBuilder(jwtConfig());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
