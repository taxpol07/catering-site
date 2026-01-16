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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. BU DOSYALAR HERKESE AÇIK OLSUN (Login Yok)
                        // css, js, images klasörleri ve ana sayfa
                        .requestMatchers(
                                "/",
                                "/index",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/webjars/**",
                                "/details/**",
                                "/category/**"
                        ).permitAll()

                        // 2. GERİ KALAN HER ŞEY (Ekleme, Silme) ŞİFRE İSTER
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) // Giriş başarılıysa Ana Sayfaya at
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Çıkış yapınca Ana Sayfaya dön
                        .permitAll()
                );

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