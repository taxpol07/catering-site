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
                .csrf(csrf -> csrf.disable()) // Form güvenliğini basitleştirir
                .authorizeHttpRequests(auth -> auth
                        // BU ADRESLER HERKESE AÇIK (Şifre İstemez)
                        .requestMatchers(
                                "/",
                                "/index",
                                "/css/**",      // Tasarım dosyaları
                                "/js/**",
                                "/images/**",
                                "/details/**",  // Ürün detay sayfası
                                "/category/**", // Kategori sayfaları
                                "/uploads/**"   // Yüklenen resimler
                        ).permitAll()

                        // GERİ KALAN HER ŞEY (Ekleme, Silme, Düzenleme) ŞİFRE İSTER
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

    // YÖNETİCİ ŞİFRESİ (Burası değişmedi)
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