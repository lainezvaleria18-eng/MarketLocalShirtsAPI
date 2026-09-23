package org.esfe.dtos.pedido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DetallePedidoGuardar implements Serializable {
    @NotNull(message = "La camisa es obligatoria")
    private Integer camisaId;

    @NotBlank(message = "La talla es obligatoria")
    @Size(max = 10, message = "La talla no debe superar 10 caracteres")
    private String talla;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
