package org.esfe.dtos.marca;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MarcaGuardar implements Serializable {
    private String nombre;
    private String descripcion;
}
