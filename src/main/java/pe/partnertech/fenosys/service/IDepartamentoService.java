/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Departamento;

import java.util.Optional;
import java.util.Set;

public interface IDepartamentoService {

    Set<Departamento> MostrarDepartamentos();

    Optional<Departamento> BuscarDepartamento_IDProvincia(Long id);
}
