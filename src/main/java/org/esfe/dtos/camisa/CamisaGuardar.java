package org.esfe.dtos.camisa;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CamisaGuardar implements Serializable {
    @NotBlank(message = "El nombre de la camisa es obligatorio")
    @Size(max = 150, message = "El nombre no debe superar 150 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripcion no debe superar 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private String imagenUrl;

    @NotBlank(message = "La talla es obligatoria")
    @Size(max = 10, message = "La talla no debe superar 10 caracteres")
    private String talla;

    private String color;

    @NotNull(message = "La categoria es obligatoria")
    private Integer categoriaId;

    @NotNull(message = "La marca es obligatoria")
    private Integer marcaId;
}
