/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.Imagen;

import java.util.Optional;

public interface IImagenService {

    Optional<Imagen> BuscarImagen_ID(Long id);

    Optional<Imagen> BuscarImagen_Nombre(String nombreimagen);

    int GuardarImagen(Imagen imagen);
}
