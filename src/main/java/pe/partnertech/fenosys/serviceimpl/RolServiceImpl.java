/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.enums.RolNombre;
import pe.partnertech.fenosys.model.Rol;
import pe.partnertech.fenosys.repository.IRolDAO;
import pe.partnertech.fenosys.service.IRolService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements IRolService {

    @Autowired
    IRolDAO data;

    @Override
    public Optional<Rol> BuscarRol_Nombre(RolNombre rol) {
        return data.findByNombreRol(rol);
    }
}
