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
                        // Statik dosyalara ve ana sayfa yollarina izin ver
                        .requestMatchers("/", "/home", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // YUKLEDIGIN RESIMLERIN GORUNMESI ICIN GEREKLI IZIN:
                        .requestMatchers("/uploads/**").permitAll()
                        // Diger tum sayfalar sifreli olsun
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        // Ozel login sayfasi olmadigi icin burayi varsayilana biraktik,
                        // boylece hata vermeden acilacak.
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    // YONETICI SIFRESI (Ayni kaldi)
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