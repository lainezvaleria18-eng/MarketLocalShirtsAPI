package org.esfe.seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String encabezado = request.getHeader("Authorization");
        if (encabezado == null || encabezado.isBlank() || !encabezado.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = encabezado.substring(7).trim();
        if (token.isEmpty() || !jwtUtil.validarToken(token)) {
            escribirNoAutorizado(response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String correo = jwtUtil.extraerCorreo(token);
                String rolToken = jwtUtil.extraerRol(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(correo);
                var autoridades = userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty()
                        ? List.of(new SimpleGrantedAuthority(rolToken))
                        : userDetails.getAuthorities();
                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                        userDetails, null, autoridades);
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        } catch (Exception ex) {
            escribirNoAutorizado(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void escribirNoAutorizado(HttpServletResponse response) throws IOException {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Token JWT ausente, invalido o expirado\"}");
    }
}
