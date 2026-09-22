package org.esfe.dtos.pedido;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PedidoModificar implements Serializable {
    private Integer id;

    @NotBlank(message = "El estado del pedido es obligatorio")
    private String estado;
}
