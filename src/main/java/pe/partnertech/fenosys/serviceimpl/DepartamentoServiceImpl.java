/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Departamento;
import pe.partnertech.fenosys.repository.IDepartamentoDAO;
import pe.partnertech.fenosys.service.IDepartamentoService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DepartamentoServiceImpl implements IDepartamentoService {

    @Autowired
    IDepartamentoDAO data;

    @Override
    public Set<Departamento> MostrarDepartamentos() {
        return data.findAllDepartamentos();
    }

    @Override
    public Optional<Departamento> BuscarDepartamento_IDProvincia(Long id) {
        return data.findDeparamentoByIDProvincia(id);
    }
}
