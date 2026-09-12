package org.esfe.servicios.interfaces;

import org.esfe.dtos.camisa.CamisaGuardar;
import org.esfe.dtos.camisa.CamisaModificar;
import org.esfe.dtos.camisa.CamisaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICamisaService {
    List<CamisaSalida> obtenerTodos();

    Page<CamisaSalida> obtenerTodosPaginados(Pageable pageable);

    CamisaSalida obtenerPorId(Integer id);

    List<CamisaSalida> buscarPorNombre(String nombre);

    List<CamisaSalida> obtenerPorCategoria(Integer categoriaId);

    CamisaSalida crear(CamisaGuardar camisaGuardar);

    CamisaSalida editar(CamisaModificar camisaModificar);

    void eliminarPorId(Integer id);
}
