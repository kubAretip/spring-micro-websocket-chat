package pl.kubaretip.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.kubaretip.authservice.security.AuthenticationFailureHandler;
import pl.kubaretip.authservice.security.AuthenticationSuccessHandler;
import pl.kubaretip.authservice.security.JWTAuthenticationFilter;
import pl.kubaretip.authservice.security.JWTBuilder;
import pl.kubaretip.authutils.jwt.JWTConfig;
import pl.kubaretip.authutils.jwt.JWTFilter;
import pl.kubaretip.authutils.jwt.JWTUtils;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] SWAGGER_AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

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
            .antMatchers(SWAGGER_AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated()
        .and()
            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .accessDeniedHandler(new AccessDeniedHandlerImpl())
        .and()
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        // @formatter:on
    }

    @Bean
    public JWTAuthenticationFilter authenticationFilter() throws Exception {
        var filter = new JWTAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler(jwtBuilder()));
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler());
        filter.setAuthenticationManager(super.authenticationManager());
        filter.setFilterProcessesUrl(jwtConfig().getAuthEndpoint());
        return filter;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtils());
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
