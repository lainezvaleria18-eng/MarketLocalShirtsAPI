package org.esfe.dtos.marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MarcaGuardar implements Serializable {
    @NotBlank(message = "El nombre de la marca es obligatorio")
    @Size(max = 80, message = "El nombre no debe superar 80 caracteres")
    private String nombre;

    @Size(max = 255, message = "La descripcion no debe superar 255 caracteres")
    private String descripcion;
}
