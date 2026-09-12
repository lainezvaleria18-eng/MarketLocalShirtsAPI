package org.esfe.repositorios;

import org.esfe.modelos.Camisa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICamisaRepository extends JpaRepository<Camisa, Integer> {
    List<Camisa> findByNombreContainingIgnoreCase(String nombre);

    List<Camisa> findByCategoriaId(Integer categoriaId);

    Page<Camisa> findByCategoriaId(Integer categoriaId, Pageable pageable);
}
