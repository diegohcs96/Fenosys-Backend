/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Ubicacion;

import java.util.Optional;
import java.util.Set;

public interface IUbicacionService {

    Optional<Ubicacion> BuscarUbicacion_PaisyDepartamentoyProvinciayDistrito(String pais, String departamento,
                                                                             String provincia, String distrito);

    Boolean ValidarUbicacion(String pais, String departamento, String provincia, String distrito);

    int GuardarUbicacion(Ubicacion ubicacion);

    Set<Ubicacion> ListaUbicaciones();
}
