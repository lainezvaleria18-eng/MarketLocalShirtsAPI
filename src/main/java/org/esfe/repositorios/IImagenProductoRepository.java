package org.esfe.repositorios;

import org.esfe.modelos.ImagenProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IImagenProductoRepository extends JpaRepository<ImagenProducto, Integer> {
    List<ImagenProducto> findByProductoId(Integer productoId);
}
