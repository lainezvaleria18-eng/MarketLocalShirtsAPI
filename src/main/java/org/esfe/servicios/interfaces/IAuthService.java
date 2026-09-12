package org.esfe.servicios.interfaces;

import org.esfe.dtos.auth.LoginGuardar;
import org.esfe.dtos.auth.LoginSalida;
import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioSalida;

public interface IAuthService {
    LoginSalida iniciarSesion(LoginGuardar loginGuardar);

    UsuarioSalida registrar(UsuarioGuardar usuarioGuardar);
}
