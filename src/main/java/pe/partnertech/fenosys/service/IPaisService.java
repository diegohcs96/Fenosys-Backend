/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Pais;

import java.util.Optional;
import java.util.Set;

public interface IPaisService {

    int GuardarPais(Pais pais);

    Set<Pais> MostrarPaises();

    Optional<Pais> BuscarPais_IDDepartamento(Long id);

    Optional<Pais> BuscarPais_Pais(String pais);

    boolean ValidarNombrePais(String pais);

    void EliminarPais(Long id);
}
