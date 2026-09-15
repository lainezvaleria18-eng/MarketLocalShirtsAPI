package org.esfe.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto_colores")
public class ProductoColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_color")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Camisa producto;

    @ManyToOne
    @JoinColumn(name = "id_color")
    private Color color;
}
