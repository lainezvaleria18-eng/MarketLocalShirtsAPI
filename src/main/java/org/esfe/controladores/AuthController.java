package org.esfe.controladores;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.esfe.seguridad.dtos.RecuperarClave;
import org.esfe.seguridad.dtos.RestablecerClave;
import org.esfe.seguridad.dtos.UsuarioLogin;
import org.esfe.seguridad.dtos.UsuarioMensaje;
import org.esfe.seguridad.dtos.UsuarioRegistrar;
import org.esfe.seguridad.dtos.UsuarioToken;
import org.esfe.seguridad.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirements
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioToken> login(@Valid @RequestBody UsuarioLogin login) {
        return ResponseEntity.ok(usuarioService.login(login));
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioToken> registro(@Valid @RequestBody UsuarioRegistrar registro) {
        return ResponseEntity.ok(usuarioService.registro(registro));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<UsuarioMensaje> forgotPassword(@Valid @RequestBody RecuperarClave solicitud) {
        return ResponseEntity.ok(usuarioService.solicitarRecuperacion(solicitud));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UsuarioMensaje> resetPassword(@Valid @RequestBody RestablecerClave solicitud) {
        return ResponseEntity.ok(usuarioService.restablecerClave(solicitud));
    }
}
