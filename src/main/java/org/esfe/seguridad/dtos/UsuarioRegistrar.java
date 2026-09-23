package org.esfe.seguridad.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioRegistrar {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
    private String nombre;

    @Size(max = 20, message = "El telefono no debe superar 20 caracteres")
    private String telefono;

    @NotBlank(message = "El login (correo) es obligatorio")
    @Email(message = "El login debe ser un correo valido")
    private String login;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    private String clave;
}
