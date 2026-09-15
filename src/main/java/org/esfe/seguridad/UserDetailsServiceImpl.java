package org.esfe.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rolNombre = usuario.getRol() != null
                ? RolNormalizador.normalizar(usuario.getRol().getNombre())
                : "CLIENTE";

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(rolNombre)
        );

        boolean deshabilitado = usuario.getActivo() != null && !usuario.getActivo();

        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                !deshabilitado,
                true,
                true,
                true,
                authorities
        );
    }
}
