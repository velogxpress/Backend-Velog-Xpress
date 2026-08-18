package com.velogexpress.securite;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;


    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("base64", new Base64PasswordEncoder());

        DelegatingPasswordEncoder encoder =
                new DelegatingPasswordEncoder(idForEncode, encoders);

        // 👇 SI PA GEN PREFIX → BASE64
        encoder.setDefaultPasswordEncoderForMatches(
                new Base64PasswordEncoder()
        );

        return encoder;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/auth/login",
                                "/api/auth/login/**",
                                "/api/register",
                                "/api/register/**",
                                "/api/order/countcolis/**",
                                "/api/ville/**",
                                "/api/region/**",
                                "/api/mainaddress/**",
                                "/api/calculatrice/**",
                                "/api/surcursal/**",
                                "/api/feedback/**",
                                "/api/recoveries/**",
                                "/uploads/**"

                                ).permitAll()
                                .requestMatchers(
                                        "/api/order",
                                        "/api/order/**",
                                        "/api/facture",
                                        "/api/facture/**",
                                        "/api/facture/whatsappfacture/**",
                                        "/api/facturedownload",
                                        "/api/facturedownload/**",
                                        "/api/facturedetails",
                                        "/api/facturedetails/**",
                                        "/api/taux",
                                        "/api/taux/**",
                                        "/api/surcursal",
                                        "/api/surcursal/**",
                                        "/api/agentsurcursal",
                                        "/api/agentsurcursal/**",
                                        "/api/amnisty",
                                        "/api/amnisty/**",
                                        "/api/amnisty/save-colis",
                                        "/api/categories",
                                        "/api/categories/**",
                                        "/api/insurance",
                                        "/api/insurance/**",
                                        "/api/feepounds",
                                        "/api/feepounds/**",
                                        "/api/specialfees",
                                        "/api/specialfees/**",
                                        "/api/cipinfees",
                                        "/api/cipinfees/**",
                                        "/api/storage",
                                        "/api/storage/**",
                                        "/api/storagedetails",
                                        "/api/storagedetails/**",
                                        "/api/sendemail",
                                        "/api/sendemail/**",
                                        "/api/store",
                                        "/api/store/**",
                                        "/api/tag",
                                        "/api/tag/**"
                                ).hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN")
                                .requestMatchers(
                                        "/api/orderdetails",
                                        "/api/orderdetails/**",
                                        "/api/orderdetails-photos",
                                        "/api/orderdetails-photos/**",
                                        "/api/orderdetails/client",
                                        "/api/orderdetails/client/**",
                                        "/api/orderdetails/findclient",
                                        "/api/orderdetails/findclient/**",
                                        "/api/clientregister",
                                        "/api/getupdatepassword/**",
                                        "/api/clientregister/**"

                                ).hasAnyAuthority("ROLE_CLIENT", "ROLE_AGENT", "ROLE_ADMIN")

                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://velogxpress.com",
                "https://www.velogxpress.com",
                "https://frontend-velog-xpress-production.up.railway.app",
                "http://localhost:3000"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
