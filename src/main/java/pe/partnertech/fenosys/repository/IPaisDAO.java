/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Pais;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IPaisDAO extends JpaRepository<Pais, Long> {

    @Query("SELECT p FROM Pais p")
    Set<Pais> findAllPaises();

    @Query(value = "SELECT dp.*, p.*, d.* " +
            "FROM departamentos_pais dp " +
            "JOIN pais p ON dp.id_pais = p.id_pais " +
            "JOIN departamento d ON dp.id_departamento = d.id_departamento " +
            "WHERE dp.id_departamento = ?1", nativeQuery = true)
    Optional<Pais> findPaisByIDDepartamento(Long id);

    Optional<Pais> findByNombrePais(String pais);

    boolean existsByNombrePais(String pais);
}
