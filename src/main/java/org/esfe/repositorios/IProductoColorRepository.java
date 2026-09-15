package org.esfe.repositorios;

import org.esfe.modelos.ProductoColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductoColorRepository extends JpaRepository<ProductoColor, Integer> {
    List<ProductoColor> findByProductoId(Integer productoId);
}
