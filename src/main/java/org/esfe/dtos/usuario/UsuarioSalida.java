package org.esfe.dtos.usuario;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioSalida implements Serializable {
    private Integer id;
    private String nombre;
    private String correo;
    private String telefono;
    private String rol;
    private Boolean activo;
}
