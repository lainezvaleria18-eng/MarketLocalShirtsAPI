package org.esfe.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "imagenes_producto")
public class ImagenProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Camisa producto;

    @Column(name = "url_imagen")
    private String urlImagen;

    private String descripcion;

    private Integer orden;

    private Boolean principal;
}
