package org.esfe.controladores;

import org.esfe.dtos.pedido.PedidoGuardar;
import org.esfe.dtos.pedido.PedidoModificar;
import org.esfe.dtos.pedido.PedidoSalida;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.servicios.interfaces.IPedidoService;
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
@RequestMapping("/api/pedidos")
public class PedidoController {
    @Autowired
    private IPedidoService pedidoService;

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<PedidoSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<PedidoSalida> pedidos = pedidoService.obtenerTodosPaginados(pageable);
        if (pedidos.hasContent())
            return ResponseEntity.ok(pedidos);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<PedidoSalida>> mostrarTodos() {
        List<PedidoSalida> pedidos = pedidoService.obtenerTodos();
        if (!pedidos.isEmpty())
            return ResponseEntity.ok(pedidos);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoSalida>> mostrarPorUsuario(@PathVariable Integer usuarioId,
                                                               Authentication authentication) {
        if (!esAdmin(authentication)) {
            UsuarioSalida actual = usuarioService.obtenerPorCorreo(authentication.getName());
            if (actual == null || !actual.getId().equals(usuarioId))
                return ResponseEntity.notFound().build();
        }
        List<PedidoSalida> pedidos = pedidoService.obtenerPorUsuario(usuarioId);
        if (!pedidos.isEmpty())
            return ResponseEntity.ok(pedidos);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoSalida> mostrarPorId(@PathVariable Integer id) {
        PedidoSalida pedido = pedidoService.obtenerPorId(id);
        if (pedido != null)
            return ResponseEntity.ok(pedido);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PedidoSalida> crear(@Valid @RequestBody PedidoGuardar pedidoGuardar,
                                             Authentication authentication) {
        if (!esAdmin(authentication)) {
            UsuarioSalida actual = usuarioService.obtenerPorCorreo(authentication.getName());
            if (actual == null)
                return ResponseEntity.notFound().build();
            pedidoGuardar.setUsuarioId(actual.getId());
        }
        PedidoSalida pedido = pedidoService.crear(pedidoGuardar);
        if (pedido != null)
            return ResponseEntity.ok(pedido);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoSalida> editar(@PathVariable Integer id,
                                              @Valid @RequestBody PedidoModificar pedidoModificar) {
        pedidoModificar.setId(id);
        PedidoSalida pedido = pedidoService.editar(pedidoModificar);
        if (pedido != null)
            return ResponseEntity.ok(pedido);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Integer id) {
        pedidoService.eliminarPorId(id);
        return ResponseEntity.ok("Pedido eliminado correctamente");
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(autoridad -> "ADMIN".equals(autoridad.getAuthority()));
    }
}
