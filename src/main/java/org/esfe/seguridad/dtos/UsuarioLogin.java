package org.esfe.seguridad.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioLogin {
    @NotBlank(message = "El login (correo) es obligatorio")
    private String login;

    @NotBlank(message = "La clave es obligatoria")
    private String clave;
}
