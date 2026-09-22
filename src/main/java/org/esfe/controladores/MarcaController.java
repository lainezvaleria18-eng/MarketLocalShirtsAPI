package org.esfe.controladores;

import org.esfe.dtos.marca.MarcaGuardar;
import org.esfe.dtos.marca.MarcaModificar;
import org.esfe.dtos.marca.MarcaSalida;
import org.esfe.servicios.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {
    @Autowired
    private IMarcaService marcaService;

    @GetMapping
    public ResponseEntity<Page<MarcaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<MarcaSalida> marcas = marcaService.obtenerTodosPaginados(pageable);
        if (marcas.hasContent())
            return ResponseEntity.ok(marcas);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/lista")
    public ResponseEntity<List<MarcaSalida>> mostrarTodos() {
        List<MarcaSalida> marcas = marcaService.obtenerTodos();
        if (!marcas.isEmpty())
            return ResponseEntity.ok(marcas);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaSalida> mostrarPorId(@PathVariable Integer id) {
        MarcaSalida marca = marcaService.obtenerPorId(id);
        if (marca != null)
            return ResponseEntity.ok(marca);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MarcaSalida> crear(@Valid @RequestBody MarcaGuardar marcaGuardar) {
        MarcaSalida marca = marcaService.crear(marcaGuardar);
        if (marca != null)
            return ResponseEntity.ok(marca);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaSalida> editar(@PathVariable Integer id, @Valid @RequestBody MarcaModificar marcaModificar) {
        marcaModificar.setId(id);
        MarcaSalida marca = marcaService.editar(marcaModificar);
        if (marca != null)
            return ResponseEntity.ok(marca);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Integer id) {
        marcaService.eliminarPorId(id);
        return ResponseEntity.ok("Marca eliminada correctamente");
    }
}
