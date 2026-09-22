package org.esfe.servicios.implementaciones;

import org.esfe.dtos.usuario.UsuarioGuardar;
import org.esfe.dtos.usuario.UsuarioModificar;
import org.esfe.dtos.usuario.UsuarioPerfilModificar;
import org.esfe.dtos.usuario.UsuarioPerfilSalida;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.seguridad.modelos.Rol;
import org.esfe.seguridad.modelos.Usuario;
import org.esfe.seguridad.repositorios.RolRepository;
import org.esfe.seguridad.repositorios.UsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("usuarioAdministracionService")
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioSalida> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .toList();
    }

    @Override
    public Page<UsuarioSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        List<UsuarioSalida> usuariosDto = page.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioSalida.class))
                .toList();
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
        Usuario usuario = Usuario.builder()
                .nombre(usuarioGuardar.getNombre())
                .correo(usuarioGuardar.getCorreo())
                .telefono(usuarioGuardar.getTelefono())
                .activo(true)
                .rol(obtenerRol(usuarioGuardar.getRol()))
                .clave(usuarioGuardar.getContrasena() != null && !usuarioGuardar.getContrasena().isBlank()
                        ? passwordEncoder.encode(usuarioGuardar.getContrasena())
                        : null)
                .build();
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
            usuario.setClave(passwordEncoder.encode(usuarioModificar.getContrasena()));
        }
        if (usuarioModificar.getRol() != null) {
            usuario.setRol(obtenerRol(usuarioModificar.getRol()));
        }
        if (usuarioModificar.getActivo() != null) {
            usuario.setActivo(usuarioModificar.getActivo());
        }
        usuario = usuarioRepository.save(usuario);
        return modelMapper.map(usuario, UsuarioSalida.class);
    }

    @Override
    public UsuarioPerfilSalida obtenerPerfil(String correo) {
        Usuario usuario = obtenerClienteAutenticado(correo);
        return mapearPerfil(usuario);
    }

    @Override
    public UsuarioPerfilSalida actualizarPerfil(String correo, UsuarioPerfilModificar perfilModificar) {
        Usuario usuario = obtenerClienteAutenticado(correo);
        Integer usuarioId = usuario.getId();

        usuarioRepository.findByCorreo(perfilModificar.getCorreo())
                .filter(existente -> !existente.getId().equals(usuarioId))
                .ifPresent(existente -> {
                    throw new IllegalArgumentException("El correo ya esta registrado por otro usuario");
                });

        usuario.setNombre(perfilModificar.getNombre().trim());
        usuario.setCorreo(perfilModificar.getCorreo().trim());
        usuario.setTelefono(perfilModificar.getTelefono().trim());
        usuario = usuarioRepository.save(usuario);
        return mapearPerfil(usuario);
    }

    @Override
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }

    private Usuario obtenerClienteAutenticado(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no existe"));
        if (usuario.getRol() == null || !"CLIENTE".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new IllegalArgumentException("Solo el cliente puede consultar o actualizar su perfil");
        }
        return usuario;
    }

    private UsuarioPerfilSalida mapearPerfil(Usuario usuario) {
        UsuarioPerfilSalida salida = new UsuarioPerfilSalida();
        salida.setId(usuario.getId());
        salida.setNombre(usuario.getNombre());
        salida.setCorreo(usuario.getCorreo());
        salida.setTelefono(usuario.getTelefono());
        return salida;
    }

    private Rol obtenerRol(String nombreRol) {
        String nombre = (nombreRol == null || nombreRol.isBlank()) ? "CLIENTE" : nombreRol;
        return rolRepository.findByNombreIgnoreCase(nombre).orElseGet(() ->
                rolRepository.findByNombreIgnoreCase("CLIENTE").orElseThrow());
    }
}
