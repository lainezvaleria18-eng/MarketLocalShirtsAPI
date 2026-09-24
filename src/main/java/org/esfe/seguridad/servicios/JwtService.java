package org.esfe.seguridad.servicios;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.esfe.seguridad.modelos.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String getToken(Usuario usuario) {
        List<String> roles = new ArrayList<>();
        roles.add(usuario.getRol().getNombre());

        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", roles);
        extraClaims.put("tipo", "ACCESO");
        return generarToken(extraClaims, usuario, 1000 * 60 * 60);
    }

    public String getTokenRecuperacion(Usuario usuario) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("tipo", "RECUPERACION");
        extraClaims.put("sello", selloClave(usuario.getClave()));
        return generarToken(extraClaims, usuario, 1000 * 60 * 30);
    }

    public boolean isTokenRecuperacionValido(String token, Usuario usuario) {
        try {
            if (isTokenExpired(token) || !"RECUPERACION".equals(getClaim(token, claims -> claims.get("tipo", String.class)))) {
                return false;
            }
            String correo = getUsernameFromToken(token);
            String sello = getClaim(token, claims -> claims.get("sello", String.class));
            return correo.equals(usuario.getUsername()) && selloClave(usuario.getClave()).equals(sello);
        } catch (Exception ex) {
            return false;
        }
    }

    private String generarToken(HashMap<String, Object> extraClaims, UserDetails usuario, long duracionMs) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + duracionMs))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            if ("RECUPERACION".equals(getClaim(token, claims -> claims.get("tipo", String.class)))) {
                return false;
            }
            final String login = getUsernameFromToken(token);
            return (login.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception ex) {
            return false;
        }
    }

    private String selloClave(String clave) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest((clave == null ? "" : clave).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest).substring(0, 16);
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el sello de recuperacion");
        }
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
