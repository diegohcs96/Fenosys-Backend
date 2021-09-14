/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Distrito;
import pe.partnertech.fenosys.repository.IDistritoDAO;
import pe.partnertech.fenosys.service.IDistritoService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DistritoServiceImpl implements IDistritoService {

    @Autowired
    IDistritoDAO data;

    @Override
    public Set<Distrito> MostrarDistritos() {
        return data.findAllDistritos();
    }

    @Override
    public Optional<Distrito> BuscarDistrito_NombreDistrito(String distrito) {
        return data.findByNombreDistrito(distrito);
    }
}
