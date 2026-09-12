package org.esfe.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DetallePedidoGuardar implements Serializable {
    private Integer camisaId;
    private Integer cantidad;
}
