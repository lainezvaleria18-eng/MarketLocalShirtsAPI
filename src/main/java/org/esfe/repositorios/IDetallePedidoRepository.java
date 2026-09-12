package org.esfe.repositorios;

import org.esfe.modelos.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
}
