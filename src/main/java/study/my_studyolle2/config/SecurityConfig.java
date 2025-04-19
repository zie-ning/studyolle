package study.my_studyolle2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    //  권한 해제 경로(permitAll): "/", "/login", "/sign-up", "check-email", "check-email-token",
    // "/email-login", "/check-email-login", "/login-link"
    // "/profile/*"(얘는 get만 허용)
    //anyRequest().authenticated(); 나머지는 로그인이 되야함

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/", "/login", "/sign-up", "/check-email", "/check-email-token",
                                        "/email-login", "/check-email-login", "/login-link").permitAll()
                                .requestMatchers(HttpMethod.GET,"/profile/*").permitAll()
                                .anyRequest().authenticated()
                        ).build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->web.ignoring().requestMatchers("/images/**", "/favicon.ico","node_modules/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
