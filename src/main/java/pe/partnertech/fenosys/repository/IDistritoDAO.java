/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Distrito;

import java.util.Set;

@Repository
public interface IDistritoDAO extends JpaRepository<Distrito, Long> {

    @Query("SELECT d FROM Distrito d")
    Set<Distrito> findAllDistritos();

    @Query(value = "SELECT dp.*, p.*, d.* " +
            "FROM distritos_provincia dp " +
            "JOIN provincia p ON dp.id_provincia = p.id_provincia " +
            "JOIN distrito d ON dp.id_distrito = d.id_distrito " +
            "WHERE dp.id_provincia = ?1", nativeQuery = true)
    Set<Distrito> findDistritosByIDProvincia(Long id);
}
