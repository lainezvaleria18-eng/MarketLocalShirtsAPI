package org.esfe.controladores;

import org.esfe.dtos.categoria.CategoriaGuardar;
import org.esfe.dtos.categoria.CategoriaModificar;
import org.esfe.dtos.categoria.CategoriaSalida;
import org.esfe.servicios.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaSalida>> mostrarTodos() {
        List<CategoriaSalida> categorias = categoriaService.obtenerTodos();
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<CategoriaSalida>> mostrarTodosPaginados(Pageable pageable) {
        Page<CategoriaSalida> categorias = categoriaService.obtenerTodosPaginados(pageable);
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaSalida> mostrarPorId(@PathVariable Integer id) {
        CategoriaSalida categoria = categoriaService.obtenerPorId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoriaSalida> crear(@RequestBody CategoriaGuardar categoriaGuardar) {
        CategoriaSalida categoria = categoriaService.crear(categoriaGuardar);
        return new ResponseEntity<>(categoria, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaSalida> editar(@PathVariable Integer id, @RequestBody CategoriaModificar categoriaModificar) {
        categoriaModificar.setId(id);
        CategoriaSalida categoria = categoriaService.editar(categoriaModificar);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminarPorId(id);
        return ResponseEntity.ok().build();
    }
}
