package com.example.__sms.config;

import com.example.__sms.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public pages
                .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()

                // Teacher-only pages (CRUD for students, teachers, departments, roles)
                .requestMatchers("/students/new", "/students/create", "/students/delete/**").hasRole("TEACHER")
                .requestMatchers("/students/edit/**", "/students/update").hasAnyRole("TEACHER", "STUDENT")
                .requestMatchers("/teachers/**").hasRole("TEACHER")
                .requestMatchers("/departments/**").hasRole("TEACHER")
                .requestMatchers("/roles/**").hasRole("TEACHER")

                // Both can view students list
                .requestMatchers("/students", "/students/").hasAnyRole("TEACHER", "STUDENT")

                // Student profile edit (limited)
                .requestMatchers("/profile/**").hasRole("STUDENT")

                // Dashboard access
                .requestMatchers("/dashboard").authenticated()

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}
