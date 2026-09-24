package org.esfe.dtos.pedido;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PedidoGuardar implements Serializable {
    private Integer usuarioId;

    @NotEmpty(message = "El pedido debe incluir al menos una camisa")
    @Valid
    private List<DetallePedidoGuardar> detalles;
}
