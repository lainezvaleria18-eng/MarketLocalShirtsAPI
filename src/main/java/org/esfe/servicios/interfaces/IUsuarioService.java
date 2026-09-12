package org.esfe.servicios.interfaces;

import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioModificar;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsuarioService {
    List<UsuarioSalida> obtenerTodos();

    Page<UsuarioSalida> obtenerTodosPaginados(Pageable pageable);

    UsuarioSalida obtenerPorId(Integer id);

    UsuarioSalida obtenerPorCorreo(String correo);

    UsuarioSalida crear(UsuarioGuardar usuarioGuardar);

    UsuarioSalida editar(UsuarioModificar usuarioModificar);

    void eliminarPorId(Integer id);
}
