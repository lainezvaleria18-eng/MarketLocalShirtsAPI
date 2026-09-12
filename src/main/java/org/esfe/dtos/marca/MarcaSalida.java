package org.esfe.dtos.marca;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MarcaSalida implements Serializable {
    private Integer id;
    private String nombre;
    private String descripcion;
}
