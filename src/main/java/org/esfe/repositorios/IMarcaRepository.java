package org.esfe.repositorios;

import org.esfe.modelos.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarcaRepository extends JpaRepository<Marca, Integer> {
}
