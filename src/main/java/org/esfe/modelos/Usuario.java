package org.esfe.modelos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    private String nombre;

    private String correo;

    private String telefono;

    private String contrasena;

    private String rol;

    private Boolean activo;

    @Column(name = "id_rol")
    private Integer idRol;
}
