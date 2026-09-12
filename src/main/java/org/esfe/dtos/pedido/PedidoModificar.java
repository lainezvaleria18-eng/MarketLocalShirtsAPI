package org.esfe.dtos.pedido;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PedidoModificar implements Serializable {
    private Integer id;
    private String estado;
}
