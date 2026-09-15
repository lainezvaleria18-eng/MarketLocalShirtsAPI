package org.esfe.repositorios;

import org.esfe.modelos.Talla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITallaRepository extends JpaRepository<Talla, Integer> {
    Optional<Talla> findByNombreIgnoreCase(String nombre);
}
