/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Provincia;

import java.util.Optional;
import java.util.Set;

public interface IProvinciaService {

    Set<Provincia> MostrarProvincias();

    Optional<Provincia> BuscarProvincia_IDDistrito(Long id);
}
