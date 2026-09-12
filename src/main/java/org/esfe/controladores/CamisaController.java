package org.esfe.controladores;

import org.esfe.dtos.camisa.CamisaGuardar;
import org.esfe.dtos.camisa.CamisaModificar;
import org.esfe.dtos.camisa.CamisaSalida;
import org.esfe.servicios.interfaces.ICamisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/camisas")
public class CamisaController {

    @Autowired
    private ICamisaService camisaService;

    @GetMapping
    public ResponseEntity<List<CamisaSalida>> mostrarTodos() {
        List<CamisaSalida> camisas = camisaService.obtenerTodos();
        if (camisas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(camisas, HttpStatus.OK);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<CamisaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<CamisaSalida> camisas = camisaService.obtenerTodosPaginados(pageable);
        if (camisas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(camisas, HttpStatus.OK);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CamisaSalida>> buscarPorNombre(@RequestParam String nombre) {
        List<CamisaSalida> camisas = camisaService.buscarPorNombre(nombre);
        if (camisas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(camisas, HttpStatus.OK);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<CamisaSalida>> mostrarPorCategoria(@PathVariable Integer categoriaId) {
        List<CamisaSalida> camisas = camisaService.obtenerPorCategoria(categoriaId);
        if (camisas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(camisas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamisaSalida> mostrarPorId(@PathVariable Integer id) {
        CamisaSalida camisa = camisaService.obtenerPorId(id);
        if (camisa == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(camisa, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CamisaSalida> crear(@RequestBody CamisaGuardar camisaGuardar) {
        CamisaSalida camisa = camisaService.crear(camisaGuardar);
        return new ResponseEntity<>(camisa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamisaSalida> editar(@PathVariable Integer id, @RequestBody CamisaModificar camisaModificar) {
        camisaModificar.setId(id);
        CamisaSalida camisa = camisaService.editar(camisaModificar);
        if (camisa == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(camisa, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        camisaService.eliminarPorId(id);
        return ResponseEntity.ok().build();
    }
}
