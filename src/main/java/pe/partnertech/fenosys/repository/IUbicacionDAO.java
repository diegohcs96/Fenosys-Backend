/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Ubicacion;

import java.util.Optional;

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
}
