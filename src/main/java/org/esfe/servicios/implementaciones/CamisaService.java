package org.esfe.servicios.implementaciones;

import org.esfe.dtos.camisa.CamisaGuardar;
import org.esfe.dtos.camisa.CamisaModificar;
import org.esfe.dtos.camisa.CamisaSalida;
import org.esfe.modelos.Camisa;
import org.esfe.modelos.Categoria;
import org.esfe.modelos.Marca;
import org.esfe.repositorios.ICamisaRepository;
import org.esfe.repositorios.ICategoriaRepository;
import org.esfe.repositorios.IMarcaRepository;
import org.esfe.servicios.interfaces.ICamisaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CamisaService implements ICamisaService {

    @Autowired
    private ICamisaRepository camisaRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CamisaSalida> obtenerTodos() {
        List<Camisa> camisas = camisaRepository.findAll();
        return camisas.stream()
                .map(camisa -> modelMapper.map(camisa, CamisaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<CamisaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Camisa> page = camisaRepository.findAll(pageable);
        List<CamisaSalida> camisasDto = page.getContent().stream()
                .map(camisa -> modelMapper.map(camisa, CamisaSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(camisasDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public CamisaSalida obtenerPorId(Integer id) {
        return camisaRepository.findById(id)
                .map(camisa -> modelMapper.map(camisa, CamisaSalida.class))
                .orElse(null);
    }

    @Override
    public List<CamisaSalida> buscarPorNombre(String nombre) {
        return camisaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(camisa -> modelMapper.map(camisa, CamisaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CamisaSalida> obtenerPorCategoria(Integer categoriaId) {
        return camisaRepository.findByCategoriaId(categoriaId).stream()
                .map(camisa -> modelMapper.map(camisa, CamisaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public CamisaSalida crear(CamisaGuardar camisaGuardar) {
        Camisa camisa = modelMapper.map(camisaGuardar, Camisa.class);
        camisa.setCategoria(obtenerCategoria(camisaGuardar.getCategoriaId()));
        camisa.setMarca(obtenerMarca(camisaGuardar.getMarcaId()));
        camisa = camisaRepository.save(camisa);
        return modelMapper.map(camisa, CamisaSalida.class);
    }

    @Override
    public CamisaSalida editar(CamisaModificar camisaModificar) {
        Camisa camisa = camisaRepository.findById(camisaModificar.getId()).orElse(null);
        if (camisa == null) {
            return null;
        }
        camisa.setNombre(camisaModificar.getNombre());
        camisa.setDescripcion(camisaModificar.getDescripcion());
        camisa.setPrecio(camisaModificar.getPrecio());
        camisa.setStock(camisaModificar.getStock());
        camisa.setImagenUrl(camisaModificar.getImagenUrl());
        camisa.setTalla(camisaModificar.getTalla());
        camisa.setColor(camisaModificar.getColor());
        camisa.setCategoria(obtenerCategoria(camisaModificar.getCategoriaId()));
        camisa.setMarca(obtenerMarca(camisaModificar.getMarcaId()));
        camisa = camisaRepository.save(camisa);
        return modelMapper.map(camisa, CamisaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        camisaRepository.deleteById(id);
    }

    private Categoria obtenerCategoria(Integer categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId).orElse(null);
    }

    private Marca obtenerMarca(Integer marcaId) {
        if (marcaId == null) {
            return null;
        }
        return marcaRepository.findById(marcaId).orElse(null);
    }
}
