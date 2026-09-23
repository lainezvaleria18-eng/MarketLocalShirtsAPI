package org.esfe.config;

import org.esfe.dtos.pedido.PedidoSalida;
import org.esfe.dtos.usuario.UsuarioSalida;
import org.esfe.modelos.Pedido;
import org.esfe.seguridad.modelos.Usuario;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Usuario, UsuarioSalida> usuarioConverter = ctx -> {
            Usuario origen = ctx.getSource();
            if (origen == null) {
                return null;
            }
            UsuarioSalida salida = new UsuarioSalida();
            salida.setId(origen.getId());
            salida.setNombre(origen.getNombre());
            salida.setCorreo(origen.getCorreo());
            salida.setTelefono(origen.getTelefono());
            salida.setActivo(origen.getActivo());
            salida.setRol(origen.getRol() != null ? origen.getRol().getNombre() : "CLIENTE");
            return salida;
        };
        modelMapper.createTypeMap(Usuario.class, UsuarioSalida.class).setConverter(usuarioConverter);

        Converter<Pedido, PedidoSalida> pedidoConverter = ctx -> {
            Pedido origen = ctx.getSource();
            if (origen == null) {
                return null;
            }
            PedidoSalida salida = ctx.getDestination() == null ? new PedidoSalida() : ctx.getDestination();
            salida.setId(origen.getId());
            salida.setFecha(origen.getFecha());
            salida.setSubtotal(origen.getSubtotal());
            salida.setIva(origen.getIva());
            salida.setTotal(origen.getTotal());
            salida.setEstado(origen.getEstadoPedido() != null ? origen.getEstadoPedido().getNombre() : null);
            if (origen.getUsuario() != null) {
                salida.setUsuario(modelMapper.map(origen.getUsuario(), UsuarioSalida.class));
            }
            if (origen.getDetalles() != null) {
                salida.setDetalles(origen.getDetalles().stream()
                        .map(detalle -> modelMapper.map(detalle, org.esfe.dtos.pedido.DetallePedidoSalida.class))
                        .toList());
            }
            return salida;
        };
        modelMapper.createTypeMap(Pedido.class, PedidoSalida.class).setConverter(pedidoConverter);

        return modelMapper;
    }
}
