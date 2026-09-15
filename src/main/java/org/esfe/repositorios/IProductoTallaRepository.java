package org.esfe.repositorios;

import org.esfe.modelos.ProductoTalla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductoTallaRepository extends JpaRepository<ProductoTalla, Integer> {
    List<ProductoTalla> findByProductoId(Integer productoId);

    Optional<ProductoTalla> findByProductoIdAndTallaId(Integer productoId, Integer tallaId);
}
