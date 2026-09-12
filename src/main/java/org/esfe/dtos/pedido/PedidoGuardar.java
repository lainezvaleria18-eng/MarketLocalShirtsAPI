package org.esfe.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PedidoGuardar implements Serializable {
    private Integer usuarioId;
    private List<DetallePedidoGuardar> detalles;
}
