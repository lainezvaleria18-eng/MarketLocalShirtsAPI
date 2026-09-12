package org.esfe.servicios.interfaces;

import org.esfe.dtos.pedido.PedidoGuardar;
import org.esfe.dtos.pedido.PedidoModificar;
import org.esfe.dtos.pedido.PedidoSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPedidoService {
    List<PedidoSalida> obtenerTodos();

    Page<PedidoSalida> obtenerTodosPaginados(Pageable pageable);

    PedidoSalida obtenerPorId(Integer id);

    List<PedidoSalida> obtenerPorUsuario(Integer usuarioId);

    PedidoSalida crear(PedidoGuardar pedidoGuardar);

    PedidoSalida editar(PedidoModificar pedidoModificar);

    void eliminarPorId(Integer id);
}
