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
                .csrf(csrf -> csrf.disable()) // Form işlemleri için basitlik sağlar
                .authorizeHttpRequests(auth -> auth
                        // BU SAYFALAR HERKESE AÇIK (Login gerekmez)
                        .requestMatchers("/", "/details/**", "/category/**", "/image/**", "/display/**", "/css/**", "/js/**", "/uploads/**").permitAll()

                        // GERİ KALAN HER ŞEY (Ekleme, Silme) ŞİFRE İSTER
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) // Giriş yapınca ana sayfaya at
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Çıkış yapınca ana sayfaya dön
                        .permitAll()
                );

        return http.build();
    }

    // 🔑 KULLANICI ADI VE ŞİFRE BURADA BELİRLENİYOR
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("polat")   // Kullanıcı adın
                .password("1234")    // Şifren
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}