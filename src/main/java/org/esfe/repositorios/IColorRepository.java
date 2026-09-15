package org.esfe.repositorios;

import org.esfe.modelos.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IColorRepository extends JpaRepository<Color, Integer> {
    Optional<Color> findByNombreIgnoreCase(String nombre);
}
