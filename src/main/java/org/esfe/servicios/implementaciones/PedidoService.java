package org.esfe.servicios.implementaciones;

import org.esfe.dtos.pedido.DetallePedidoGuardar;
import org.esfe.dtos.pedido.PedidoGuardar;
import org.esfe.dtos.pedido.PedidoModificar;
import org.esfe.dtos.pedido.PedidoSalida;
import org.esfe.modelos.Camisa;
import org.esfe.modelos.DetallePedido;
import org.esfe.modelos.Pedido;
import org.esfe.modelos.Usuario;
import org.esfe.repositorios.ICamisaRepository;
import org.esfe.repositorios.IPedidoRepository;
import org.esfe.repositorios.IUsuarioRepository;
import org.esfe.servicios.interfaces.IPedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService implements IPedidoService {

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ICamisaRepository camisaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<PedidoSalida> obtenerTodos() {
        return pedidoRepository.findAll().stream()
                .map(pedido -> modelMapper.map(pedido, PedidoSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<PedidoSalida> obtenerTodosPaginados(Pageable pageable) {
        Page<Pedido> page = pedidoRepository.findAll(pageable);
        List<PedidoSalida> pedidosDto = page.getContent().stream()
                .map(pedido -> modelMapper.map(pedido, PedidoSalida.class))
                .collect(Collectors.toList());
        return new PageImpl<>(pedidosDto, page.getPageable(), page.getTotalElements());
    }

    @Override
    public PedidoSalida obtenerPorId(Integer id) {
        return pedidoRepository.findById(id)
                .map(pedido -> modelMapper.map(pedido, PedidoSalida.class))
                .orElse(null);
    }

    @Override
    public List<PedidoSalida> obtenerPorUsuario(Integer usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoSalida.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PedidoSalida crear(PedidoGuardar pedidoGuardar) {
        Usuario usuario = usuarioRepository.findById(pedidoGuardar.getUsuarioId()).orElse(null);
        if (usuario == null || pedidoGuardar.getDetalles() == null || pedidoGuardar.getDetalles().isEmpty()) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("CONFIRMADO");
        pedido.setDetalles(new ArrayList<>());

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedidoGuardar detalleGuardar : pedidoGuardar.getDetalles()) {
            Camisa camisa = camisaRepository.findById(detalleGuardar.getCamisaId()).orElse(null);
            if (camisa == null || detalleGuardar.getCantidad() == null || detalleGuardar.getCantidad() <= 0) {
                return null;
            }
            if (camisa.getStock() == null || camisa.getStock() < detalleGuardar.getCantidad()) {
                return null;
            }

            camisa.setStock(camisa.getStock() - detalleGuardar.getCantidad());
            camisaRepository.save(camisa);

            BigDecimal subtotal = camisa.getPrecio().multiply(BigDecimal.valueOf(detalleGuardar.getCantidad()));
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setCamisa(camisa);
            detalle.setCantidad(detalleGuardar.getCantidad());
            detalle.setPrecioUnitario(camisa.getPrecio());
            detalle.setSubtotal(subtotal);
            pedido.getDetalles().add(detalle);
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);
        return modelMapper.map(pedido, PedidoSalida.class);
    }

    @Override
    public PedidoSalida editar(PedidoModificar pedidoModificar) {
        Pedido pedido = pedidoRepository.findById(pedidoModificar.getId()).orElse(null);
        if (pedido == null) {
            return null;
        }
        pedido.setEstado(pedidoModificar.getEstado());
        pedido = pedidoRepository.save(pedido);
        return modelMapper.map(pedido, PedidoSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
