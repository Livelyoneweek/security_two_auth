package study.security_nopassword.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import study.security_nopassword.config.jwt.AuthenticationManagerImpl;
import study.security_nopassword.config.jwt.JwtAuthenticationFilter;
import study.security_nopassword.config.jwt.JwtAuthorizationFilter;
import study.security_nopassword.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final AuthenticationManagerImpl authenticationManager;

//    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(c -> c.disable())
                .cors(c -> c.disable())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(c -> c.frameOptions(f -> f.disable()).disable())
                .authorizeHttpRequests(auth -> {
                    try {
                        auth.requestMatchers("/v1/api/user/join").permitAll()
                                .requestMatchers("/v1/api/user/login").permitAll()
                                        .anyRequest().authenticated();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionHandling(handler -> {
                    handler.accessDeniedHandler(accessDeniedHandler());
                    handler.authenticationEntryPoint(authenticationEntryPointCustom());
                })
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        return http.build();

    }


//    @Bean
//    @Primary
//    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuditorAware<Long> auditorAware(){
        return new AuditorAwareImpl();
    }

    private AccessDeniedHandlerCustom accessDeniedHandler() {
        return new AccessDeniedHandlerCustom();
    }

    private AuthenticationEntryPointCustom authenticationEntryPointCustom() {
        return new AuthenticationEntryPointCustom();
    }
}
