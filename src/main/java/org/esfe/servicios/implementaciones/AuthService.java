package org.esfe.servicios.implementaciones;

import org.esfe.dtos.auth.LoginGuardar;
import org.esfe.dtos.auth.LoginSalida;
import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.seguridad.JwtUtil;
import org.esfe.servicios.interfaces.IAuthService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public LoginSalida iniciarSesion(LoginGuardar loginGuardar) {
        Usuario usuario = usuarioRepository.findByCorreo(loginGuardar.getCorreo()).orElse(null);
        if (usuario == null || loginGuardar.getContrasena() == null) {
            return null;
        }
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            return null;
        }

        boolean coincide = false;
        try {
            coincide = passwordEncoder.matches(loginGuardar.getContrasena(), usuario.getContrasena());
        } catch (Exception ignored) {
            coincide = false;
        }
        if (!coincide && loginGuardar.getContrasena().equals(usuario.getContrasena())) {
            usuario.setContrasena(passwordEncoder.encode(loginGuardar.getContrasena()));
            usuario = usuarioRepository.save(usuario);
            coincide = true;
        }
        if (!coincide) {
            return null;
        }

        LoginSalida salida = new LoginSalida();
        salida.setToken(jwtUtil.generarToken(usuario));
        salida.setTipoToken("Bearer");
        salida.setUsuario(modelMapper.map(usuario, UsuarioSalida.class));
        return salida;
    }

    @Override
    public UsuarioSalida registrar(UsuarioGuardar usuarioGuardar) {
        if (usuarioRepository.findByCorreo(usuarioGuardar.getCorreo()).isPresent()) {
            return null;
        }
        if (usuarioRepository.count() == 0) {
            usuarioGuardar.setRol("ADMIN");
        } else {
            usuarioGuardar.setRol("CLIENTE");
        }
        return usuarioService.crear(usuarioGuardar);
    }
}
