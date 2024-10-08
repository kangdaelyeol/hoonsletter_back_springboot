package com.example.hoonsletter_back_springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;

  @Autowired
  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
        .httpBasic(AbstractHttpConfigurer::disable) // (customizer -> customizer.disable()) (same)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> {
          CorsConfiguration corsConfiguration = new CorsConfiguration();

          corsConfiguration.addAllowedOrigin("*");
          corsConfiguration.addAllowedMethod("*");
          corsConfiguration.addAllowedHeader("*");

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

          source.registerCorsConfiguration("/**", corsConfiguration);

          cors.configurationSource(source);
        })
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement((customizer) ->
          customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests((customizer) ->
          customizer
              .requestMatchers("/letter/create").authenticated()
              .anyRequest().permitAll()
        )
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
