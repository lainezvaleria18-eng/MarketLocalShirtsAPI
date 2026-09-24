package org.esfe.controladores;

import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioModificar;
import org.esfe.dtos.usuario.UsuarioPerfilModificar;
import org.esfe.dtos.usuario.UsuarioPerfilSalida;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<UsuarioSalida> usuarios = usuarioService.obtenerTodosPaginados(pageable);
        if (usuarios.hasContent())
            return ResponseEntity.ok(usuarios);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioSalida>> mostrarTodos() {
        List<UsuarioSalida> usuarios = usuarioService.obtenerTodos();
        if (!usuarios.isEmpty())
            return ResponseEntity.ok(usuarios);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioPerfilSalida> mostrarPerfil(Authentication authentication) {
        UsuarioPerfilSalida perfil = usuarioService.obtenerPerfil(authentication.getName());
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/perfil")
    public ResponseEntity<UsuarioPerfilSalida> editarPerfil(Authentication authentication,
                                                           @Valid @RequestBody UsuarioPerfilModificar perfilModificar) {
        UsuarioPerfilSalida perfil = usuarioService.actualizarPerfil(authentication.getName(), perfilModificar);
        return ResponseEntity.ok(perfil);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSalida> mostrarPorId(@PathVariable Integer id) {
        UsuarioSalida usuario = usuarioService.obtenerPorId(id);
        if (usuario != null)
            return ResponseEntity.ok(usuario);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UsuarioSalida> crear(@Valid @RequestBody UsuarioGuardar usuarioGuardar) {
        UsuarioSalida usuario = usuarioService.crear(usuarioGuardar);
        if (usuario != null)
            return ResponseEntity.ok(usuario);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSalida> editar(@PathVariable Integer id, @Valid @RequestBody UsuarioModificar usuarioModificar) {
        usuarioModificar.setId(id);
        UsuarioSalida usuario = usuarioService.editar(usuarioModificar);
        if (usuario != null)
            return ResponseEntity.ok(usuario);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Integer id) {
        usuarioService.eliminarPorId(id);
        return ResponseEntity.ok("Usuario eliminado correctamente");
    }
}
