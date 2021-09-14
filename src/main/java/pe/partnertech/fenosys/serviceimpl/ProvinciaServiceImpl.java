/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Provincia;
import pe.partnertech.fenosys.repository.IProvinciaDAO;
import pe.partnertech.fenosys.service.IProvinciaService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ProvinciaServiceImpl implements IProvinciaService {

    @Autowired
    IProvinciaDAO data;

    @Override
    public Set<Provincia> MostrarProvincias() {
        return data.findAllProvincias();
    }

    @Override
    public Optional<Provincia> BuscarProvincia_IDDistrito(Long id) {
        return data.findProvinciaByIDDistrito(id);
    }
}
