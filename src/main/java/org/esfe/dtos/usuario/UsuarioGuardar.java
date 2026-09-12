package org.esfe.dtos.usuario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioGuardar implements Serializable {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private String rol;
}
