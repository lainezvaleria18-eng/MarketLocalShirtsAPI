package org.esfe.dtos.pedido;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.usuario.UsuarioSalida;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PedidoSalida implements Serializable {
    private Integer id;
    private UsuarioSalida usuario;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private List<DetallePedidoSalida> detalles;
}
