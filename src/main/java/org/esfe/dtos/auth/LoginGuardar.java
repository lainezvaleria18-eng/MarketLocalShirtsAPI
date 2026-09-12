package org.esfe.dtos.auth;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginGuardar implements Serializable {
    private String correo;
    private String contrasena;
}
