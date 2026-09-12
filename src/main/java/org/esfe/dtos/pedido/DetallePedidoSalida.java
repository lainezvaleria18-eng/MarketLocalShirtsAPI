package org.esfe.dtos.pedido;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.camisa.CamisaSalida;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class DetallePedidoSalida implements Serializable {
    private Integer id;
    private CamisaSalida camisa;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
