package uz.pdp.learningcenterapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import uz.pdp.learningcenterapp.config.failures.SuccessHandler;
import uz.pdp.learningcenterapp.entity.enums.RoleType;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final SuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(m->{
                    m.requestMatchers("/login", "/register", "/logout").permitAll()
                            .requestMatchers("/","/student").hasRole("STUDENT")
                            .requestMatchers("/","/admin", "/admin/**", "/timetable").hasRole("ADMIN")
                            .anyRequest().authenticated();
                });
        http.formLogin(m->{
           m
                   .loginPage("/login")
                   .successHandler(successHandler);
        });

        http.userDetailsService(customUserDetailService);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
