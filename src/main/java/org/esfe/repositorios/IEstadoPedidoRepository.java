package org.esfe.repositorios;

import org.esfe.modelos.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {
    Optional<EstadoPedido> findByNombreIgnoreCase(String nombre);
}
