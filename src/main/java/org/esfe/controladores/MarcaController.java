package org.esfe.controladores;

import org.esfe.dtos.marca.MarcaGuardar;
import org.esfe.dtos.marca.MarcaModificar;
import org.esfe.dtos.marca.MarcaSalida;
import org.esfe.servicios.interfaces.IMarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private IMarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaSalida>> mostrarTodos() {
        List<MarcaSalida> marcas = marcaService.obtenerTodos();
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(marcas, HttpStatus.OK);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<MarcaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<MarcaSalida> marcas = marcaService.obtenerTodosPaginados(pageable);
        if (marcas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(marcas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaSalida> mostrarPorId(@PathVariable Integer id) {
        MarcaSalida marca = marcaService.obtenerPorId(id);
        if (marca == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MarcaSalida> crear(@RequestBody MarcaGuardar marcaGuardar) {
        MarcaSalida marca = marcaService.crear(marcaGuardar);
        return new ResponseEntity<>(marca, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarcaSalida> editar(@PathVariable Integer id, @RequestBody MarcaModificar marcaModificar) {
        marcaModificar.setId(id);
        MarcaSalida marca = marcaService.editar(marcaModificar);
        if (marca == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(marca, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        marcaService.eliminarPorId(id);
        return ResponseEntity.ok().build();
    }
}
