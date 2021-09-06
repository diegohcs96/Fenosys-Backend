/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.repository.IImagenDAO;
import pe.partnertech.fenosys.service.IImagenService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ImagenServiceImpl implements IImagenService {

    @Autowired
    IImagenDAO data;

    @Override
    public Optional<Imagen> BuscarImagen_ID(Long id) {
        return data.findById(id);
    }

    @Override
    public Optional<Imagen> BuscarImagen_Nombre(String nombreimagen) {
        return data.findByNombreImagen(nombreimagen);
    }

    @Override
    public int GuardarImagen(Imagen imagen) {
        int rpta = 0;

        Imagen i = data.save(imagen);

        if (!i.equals(null)) {

            rpta = 1;
        }

        return rpta;
    }
}
