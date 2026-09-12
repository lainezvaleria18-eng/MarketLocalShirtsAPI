package org.esfe.dtos.usuario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioModificar implements Serializable {
    private Integer id;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private String rol;
    private Boolean activo;
}
