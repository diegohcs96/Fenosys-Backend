/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Distrito;

import java.util.Optional;
import java.util.Set;

public interface IDistritoService {

    Set<Distrito> MostrarDistritos();

    Set<Distrito> BuscarDistritos_IDProvincia(Long id);

    Optional<Distrito> BuscarDistrito_ID(Long id);
}
