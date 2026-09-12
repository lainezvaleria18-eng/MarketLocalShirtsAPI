package org.esfe.controladores;

import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioModificar;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioSalida>> mostrarTodos() {
        List<UsuarioSalida> usuarios = usuarioService.obtenerTodos();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<UsuarioSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<UsuarioSalida> usuarios = usuarioService.obtenerTodosPaginados(pageable);
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<UsuarioSalida> mostrarPorCorreo(@PathVariable String correo) {
        UsuarioSalida usuario = usuarioService.obtenerPorCorreo(correo);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSalida> mostrarPorId(@PathVariable Integer id) {
        UsuarioSalida usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UsuarioSalida> crear(@RequestBody UsuarioGuardar usuarioGuardar) {
        UsuarioSalida usuario = usuarioService.crear(usuarioGuardar);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSalida> editar(@PathVariable Integer id, @RequestBody UsuarioModificar usuarioModificar) {
        usuarioModificar.setId(id);
        UsuarioSalida usuario = usuarioService.editar(usuarioModificar);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminarPorId(id);
        return ResponseEntity.ok().build();
    }
}
