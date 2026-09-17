package org.esfe.config;

import org.esfe.seguridad.configuracion.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/camisas/**", "/api/categorias/**", "/api/marcas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/camisas/**", "/api/categorias/**", "/api/marcas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/camisas/**", "/api/categorias/**", "/api/marcas/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/camisas/**", "/api/categorias/**", "/api/marcas/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos").hasAnyAuthority("ADMIN", "CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/usuario/**").hasAnyAuthority("ADMIN", "CLIENTE")
                        .requestMatchers("/api/pedidos/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
