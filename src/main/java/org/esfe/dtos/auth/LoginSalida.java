package org.esfe.dtos.auth;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.usuario.UsuarioSalida;

import java.io.Serializable;

@Getter
@Setter
public class LoginSalida implements Serializable {
    private String token;
    private String tipoToken;
    private UsuarioSalida usuario;
}
