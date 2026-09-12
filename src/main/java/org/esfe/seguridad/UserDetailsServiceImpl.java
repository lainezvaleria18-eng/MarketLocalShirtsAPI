package org.esfe.seguridad;

import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String rol = usuario.getRol() == null ? "CLIENTE" : usuario.getRol().replace("ROLE_", "");
        boolean deshabilitado = usuario.getActivo() != null && !usuario.getActivo();

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .disabled(deshabilitado)
                .roles(rol)
                .build();
    }
}
