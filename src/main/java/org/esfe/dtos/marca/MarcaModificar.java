package org.esfe.dtos.marca;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MarcaModificar implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
}
