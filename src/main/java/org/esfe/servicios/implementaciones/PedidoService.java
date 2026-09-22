package org.esfe.servicios.implementaciones;

import org.esfe.dtos.pedido.DetallePedidoGuardar;
import org.esfe.dtos.pedido.PedidoGuardar;
import org.esfe.dtos.pedido.PedidoModificar;
import org.esfe.dtos.pedido.PedidoSalida;
import org.esfe.modelos.Camisa;
import org.esfe.modelos.DetallePedido;
import org.esfe.modelos.EstadoPedido;
import org.esfe.modelos.Pedido;
import org.esfe.modelos.ProductoTalla;
import org.esfe.repositorios.ICamisaRepository;
import org.esfe.repositorios.IEstadoPedidoRepository;
import org.esfe.repositorios.IPedidoRepository;
import org.esfe.repositorios.IProductoTallaRepository;
import org.esfe.seguridad.modelos.Usuario;
import org.esfe.seguridad.repositorios.UsuarioRepository;
import org.esfe.servicios.StockInsuficienteException;
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

    private static final BigDecimal IVA = new BigDecimal("0.13");

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ICamisaRepository camisaRepository;

    @Autowired
    private IProductoTallaRepository productoTallaRepository;

    @Autowired
    private IEstadoPedidoRepository estadoPedidoRepository;

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
        if (pedidoGuardar.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario del pedido es obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(pedidoGuardar.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        EstadoPedido estado = estadoPedidoRepository.findByNombreIgnoreCase("CONFIRMADO")
                .orElseThrow(() -> new IllegalArgumentException("No existe el estado CONFIRMADO"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstadoPedido(estado);
        pedido.setDetalles(new ArrayList<>());

        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetallePedidoGuardar detalleGuardar : pedidoGuardar.getDetalles()) {
            Camisa camisa = camisaRepository.findById(detalleGuardar.getCamisaId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La camisa " + detalleGuardar.getCamisaId() + " no existe"));

            List<ProductoTalla> tallas = productoTallaRepository.findByProductoId(camisa.getId());
            if (tallas.isEmpty()) {
                throw new StockInsuficienteException(
                        "La camisa '" + camisa.getNombre() + "' no tiene stock registrado");
            }

            ProductoTalla productoTalla = tallas.get(0);
            int stockActual = productoTalla.getStock() == null ? 0 : productoTalla.getStock();
            int cantidad = detalleGuardar.getCantidad();
            if (stockActual < cantidad) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + camisa.getNombre()
                                + "'. Disponible: " + stockActual + ", solicitado: " + cantidad);
            }

            productoTalla.setStock(stockActual - cantidad);
            productoTallaRepository.save(productoTalla);

            BigDecimal linea = camisa.getPrecio().multiply(BigDecimal.valueOf(cantidad));
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setCamisa(camisa);
            detalle.setTalla(productoTalla.getTalla());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(camisa.getPrecio());
            detalle.setSubtotal(linea);
            pedido.getDetalles().add(detalle);
            subtotal = subtotal.add(linea);
        }

        BigDecimal iva = subtotal.multiply(IVA);
        pedido.setSubtotal(subtotal);
        pedido.setIva(iva);
        pedido.setTotal(subtotal.add(iva));
        pedido = pedidoRepository.save(pedido);
        return modelMapper.map(pedido, PedidoSalida.class);
    }

    @Override
    public PedidoSalida editar(PedidoModificar pedidoModificar) {
        Pedido pedido = pedidoRepository.findById(pedidoModificar.getId()).orElse(null);
        if (pedido == null) {
            return null;
        }
        estadoPedidoRepository.findByNombreIgnoreCase(pedidoModificar.getEstado())
                .ifPresent(pedido::setEstadoPedido);
        pedido = pedidoRepository.save(pedido);
        return modelMapper.map(pedido, PedidoSalida.class);
    }

    @Override
    public void eliminarPorId(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
