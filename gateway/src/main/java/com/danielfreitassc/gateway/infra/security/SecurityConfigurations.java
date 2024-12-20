package com.danielfreitassc.gateway.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurations {
    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                .requestMatchers(HttpMethod.POST,"/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/user").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/user/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/user/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/user/{id}").hasRole("ADMIN")
                
                
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET,"/validation").hasAnyRole("ADMIN","PROFESSOR","ALUNO")


                //Permite o swagger
                .requestMatchers(HttpMethod.GET,"/v3/api-docs/swagger-config").permitAll()
                .requestMatchers(HttpMethod.GET,"/v3/api-docs").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui/**").permitAll()


                // Configuração para endpoint de erro
                .requestMatchers("/error").anonymous()
                .anyRequest().authenticated()

                ).addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
