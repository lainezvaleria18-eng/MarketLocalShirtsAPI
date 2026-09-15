package org.esfe.controladores;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.esfe.dtos.auth.LoginGuardar;
import org.esfe.dtos.auth.LoginSalida;
import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.servicios.interfaces.IAuthService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IUsuarioService usuarioService;

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<LoginSalida> iniciarSesion(@RequestBody LoginGuardar loginGuardar) {
        LoginSalida login = authService.iniciarSesion(loginGuardar);
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @SecurityRequirements
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioSalida> registrar(@RequestBody UsuarioGuardar usuarioGuardar) {
        UsuarioSalida usuario = authService.registrar(usuarioGuardar);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioSalida> perfil(Authentication authentication) {
        UsuarioSalida usuario = usuarioService.obtenerPorCorreo(authentication.getName());
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
