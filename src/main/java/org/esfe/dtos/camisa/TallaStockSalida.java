package org.esfe.dtos.camisa;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TallaStockSalida implements Serializable {
    private String talla;
    private Integer stock;
    private Boolean agotado;
}
