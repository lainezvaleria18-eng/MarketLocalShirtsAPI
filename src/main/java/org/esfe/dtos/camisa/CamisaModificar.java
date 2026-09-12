package org.esfe.dtos.camisa;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CamisaModificar implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private String talla;
    private String color;
    private Integer categoriaId;
    private Integer marcaId;
}
