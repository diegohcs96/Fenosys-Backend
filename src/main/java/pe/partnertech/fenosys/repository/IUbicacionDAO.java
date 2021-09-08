/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Ubicacion;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IUbicacionDAO extends JpaRepository<Ubicacion, Integer> {

    Optional<Ubicacion> findByPaisUbicacionAndDepartamentoUbicacionAndProvinciaUbicacionAndDistritoUbicacion(String pais,
                                                                                                             String departamento,
                                                                                                             String provincia,
                                                                                                             String distrito);

    boolean existsByPaisUbicacionAndDepartamentoUbicacionAndProvinciaUbicacionAndDistritoUbicacion(String pais,
                                                                                                   String departamento,
                                                                                                   String provincia,
                                                                                                   String distrito);

    @Query("SELECT u FROM Ubicacion u GROUP BY u.paisUbicacion")
    Set<Ubicacion> findAllPaises();

    @Query("SELECT u FROM Ubicacion u GROUP BY u.paisUbicacion, u.departamentoUbicacion")
    Set<Ubicacion> findAllDepartamentos();

    @Query("SELECT u FROM Ubicacion u GROUP BY u.paisUbicacion, u.departamentoUbicacion, u.provinciaUbicacion")
    Set<Ubicacion> findAllProvincias();

    @Query("SELECT u FROM Ubicacion u GROUP BY u.paisUbicacion, u.departamentoUbicacion, u.provinciaUbicacion, u.distritoUbicacion")
    Set<Ubicacion> findAllDistritos();
}
