package org.esfe.dtos.camisa;

import lombok.Getter;
import lombok.Setter;
import org.esfe.dtos.categoria.CategoriaSalida;
import org.esfe.dtos.marca.MarcaSalida;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CamisaSalida implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private Boolean agotado;
    private String imagenUrl;
    private String talla;
    private String color;
    private List<TallaStockSalida> tallas;
    private CategoriaSalida categoria;
    private MarcaSalida marca;
}
