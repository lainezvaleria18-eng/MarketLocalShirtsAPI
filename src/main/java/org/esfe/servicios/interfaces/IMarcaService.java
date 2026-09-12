package org.esfe.servicios.interfaces;

import org.esfe.dtos.marca.MarcaGuardar;
import org.esfe.dtos.marca.MarcaModificar;
import org.esfe.dtos.marca.MarcaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMarcaService {
    List<MarcaSalida> obtenerTodos();

    Page<MarcaSalida> obtenerTodosPaginados(Pageable pageable);

    MarcaSalida obtenerPorId(Integer id);

    MarcaSalida crear(MarcaGuardar marcaGuardar);

    MarcaSalida editar(MarcaModificar marcaModificar);

    void eliminarPorId(Integer id);
}
