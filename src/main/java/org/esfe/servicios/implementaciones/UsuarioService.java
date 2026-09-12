package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioModificar;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioSalida> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<UsuarioSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        List<UsuarioSalida> usuariosDto = page.getContent().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usuariosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public UsuarioSalida obtenerPorId(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .orElse(null);
    }

    @Override
    public UsuarioSalida obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .orElse(null);
    }

    @Override
    public UsuarioSalida crear(UsuarioGuardar usuarioGuardar) {
        Usuario usuario = modelMapper.map(usuarioGuardar, Usuario.class);
        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("CLIENTE");
        }
        usuario.setActivo(true);
        if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {
            usuario.setIdRol(1);
        } else {
            usuario.setIdRol(2);
        }
        if (usuarioGuardar.getContrasena() != null && !usuarioGuardar.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioGuardar.getContrasena()));
        }
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioSalida.class);
    }

    @Override
    public UsuarioSalida editar(UsuarioModificar usuarioModificar) {
        Usuario usuario = usuarioRepository.findById(usuarioModificar.getId()).orElse(null);
        if (usuario == null) {
            return null;
        }
        usuario.setNombre(usuarioModificar.getNombre());
        usuario.setCorreo(usuarioModificar.getCorreo());
        usuario.setTelefono(usuarioModificar.getTelefono());
        if (usuarioModificar.getContrasena() != null && !usuarioModificar.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(usuarioModificar.getContrasena()));
        }
        if (usuarioModificar.getRol() != null) {
            usuario.setRol(usuarioModificar.getRol());
        }
        if (usuarioModificar.getActivo() != null) {
            usuario.setActivo(usuarioModificar.getActivo());
        }
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
