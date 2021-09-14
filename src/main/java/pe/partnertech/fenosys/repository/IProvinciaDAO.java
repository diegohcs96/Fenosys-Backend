/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Provincia;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IProvinciaDAO extends JpaRepository<Provincia, Long> {

    @Query("SELECT p FROM Provincia p")
    Set<Provincia> findAllProvincias();

    @Query(value = "SELECT dp.*, p.*, d.* " +
            "FROM distritos_provincia dp " +
            "JOIN provincia p ON dp.id_provincia = p.id_provincia " +
            "JOIN distrito d ON dp.id_distrito = d.id_distrito " +
            "WHERE dp.id_distrito = ?1", nativeQuery = true)
    Optional<Provincia> findProvinciaByIDDistrito(Long id);
}
