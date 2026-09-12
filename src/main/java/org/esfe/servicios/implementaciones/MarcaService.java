package org.esfe.servicios.implementaciones;

import org.esfe.dtos.marca.MarcaGuardar;
import org.esfe.dtos.marca.MarcaModificar;
import org.esfe.dtos.marca.MarcaSalida;
import org.esfe.modelos.Marca;
import org.esfe.repositorios.IMarcaRepository;
import org.esfe.servicios.interfaces.IMarcaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcaService implements IMarcaService {

    @Autowired
    private IMarcaRepository marcaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<MarcaSalida> obtenerTodos() {
        List<Marca> marcas = marcaRepository.findAll();
        return marcas.stream()
                .map(marca -> modelMapper.map(marca, MarcaSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MarcaSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Marca> page = marcaRepository.findAll(pageable);
        List<MarcaSalida> marcasDto = page.getContent().stream()
                .map(marca -> modelMapper.map(marca, MarcaSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(marcasDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public MarcaSalida obtenerPorId(Integer id) {
        return marcaRepository.findById(id)
                .map(marca -> modelMapper.map(marca, MarcaSalida.class))
                .orElse(null);
    }

    @Override
    public MarcaSalida crear(MarcaGuardar marcaGuardar) {
        Marca marca = modelMapper.map(marcaGuardar, Marca.class);
        marca = marcaRepository.save(marca);
        return modelMapper.map(marca, MarcaSalida.class);
    }

    @Override
    public MarcaSalida editar(MarcaModificar marcaModificar) {
        Marca marca = marcaRepository.findById(marcaModificar.getId()).orElse(null);
        if (marca == null) {
            return null;
        }
        modelMapper.map(marcaModificar, marca);
        marca = marcaRepository.save(marca);
        return modelMapper.map(marca, MarcaSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        marcaRepository.deleteById(id);
    }
}
