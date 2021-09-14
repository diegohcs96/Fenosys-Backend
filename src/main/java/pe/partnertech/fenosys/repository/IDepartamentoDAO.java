/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Departamento;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IDepartamentoDAO extends JpaRepository<Departamento, Long> {

    @Query("SELECT d FROM Departamento d")
    Set<Departamento> findAllDepartamentos();

    @Query(value = "SELECT pd.*, d.*, p.* " +
            "FROM provincias_departamento pd " +
            "JOIN departamento d ON pd.id_departamento = d.id_departamento " +
            "JOIN provincia p ON pd.id_provincia = p.id_provincia " +
            "WHERE pd.id_provincia = ?1", nativeQuery = true)
    Optional<Departamento> findDeparamentoByIDProvincia(Long id);
}
