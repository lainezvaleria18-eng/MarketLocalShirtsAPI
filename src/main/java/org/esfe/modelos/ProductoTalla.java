package org.esfe.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "producto_tallas")
public class ProductoTalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto_talla")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Camisa producto;

    @ManyToOne
    @JoinColumn(name = "id_talla")
    private Talla talla;

    private Integer stock;
}
