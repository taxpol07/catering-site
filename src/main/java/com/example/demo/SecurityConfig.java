package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // HERKESİN GİREBİLECEĞİ SAYFALAR (Detay sayfası eklendi)
                        .requestMatchers(
                                "/",
                                "/home",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**",
                                "/details/**",   // <-- ARTIK MÜŞTERİLER GİREBİLİR
                                "/category/**"   // <-- KATEGORİLER DE AÇIK
                        ).permitAll()
                        // DİĞER HER YER ŞİFRELİ (Ürün ekleme, silme vb.)
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    // YÖNETİCİ ŞİFRESİ
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("polat")
                .password("1234")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}