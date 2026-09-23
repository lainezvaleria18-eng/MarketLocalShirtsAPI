package org.esfe.seguridad.servicios;

import org.esfe.seguridad.dtos.RecuperarClave;
import org.esfe.seguridad.dtos.RestablecerClave;
import org.esfe.seguridad.dtos.UsuarioLogin;
import org.esfe.seguridad.dtos.UsuarioMensaje;
import org.esfe.seguridad.dtos.UsuarioRegistrar;
import org.esfe.seguridad.dtos.UsuarioToken;
import org.esfe.seguridad.modelos.Usuario;
import org.esfe.seguridad.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private RolService rolService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CorreoService correoService;

    public UsuarioToken login(UsuarioLogin loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getClave()));
        Usuario usuario = userRepository.findByCorreo(loginRequest.getLogin()).orElseThrow();
        String token = jwtService.getToken(usuario);
        return UsuarioToken.builder()
                .token(token)
                .build();
    }

    public UsuarioToken registro(UsuarioRegistrar registroRequest) {
        String correo = registroRequest.getLogin().trim();
        if (userRepository.findByCorreo(correo).isPresent()) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(registroRequest.getNombre())
                .telefono(registroRequest.getTelefono())
                .correo(correo)
                .clave(passwordEncoder.encode(registroRequest.getClave()))
                .activo(true)
                .rol(rolService.obtenerCliente())
                .build();

        userRepository.save(usuario);

        return UsuarioToken.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }

    public UsuarioMensaje solicitarRecuperacion(RecuperarClave solicitud) {
        String mensaje = "Si el correo esta registrado, se envio un enlace valido por 30 minutos";
        userRepository.findByCorreo(solicitud.getCorreo()).ifPresent(usuario -> {
            String token = jwtService.getTokenRecuperacion(usuario);
            correoService.enviarEnlaceRecuperacion(usuario.getCorreo(), token);
        });
        return new UsuarioMensaje(mensaje);
    }

    public UsuarioMensaje restablecerClave(RestablecerClave solicitud) {
        String correo;
        try {
            correo = jwtService.getUsernameFromToken(solicitud.getToken());
        } catch (Exception ex) {
            throw new IllegalArgumentException("El token de recuperacion no es valido o ya expiro");
        }

        Usuario usuario = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));

        if (!jwtService.isTokenRecuperacionValido(solicitud.getToken(), usuario)) {
            throw new IllegalArgumentException("El token de recuperacion no es valido o ya expiro");
        }

        usuario.setClave(passwordEncoder.encode(solicitud.getNuevaClave()));
        userRepository.save(usuario);
        return new UsuarioMensaje("La contrasena se restablecio correctamente. Inicie sesion de nuevo");
    }
}
