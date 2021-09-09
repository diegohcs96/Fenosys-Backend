/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.RegistroDiario;
import pe.partnertech.fenosys.repository.IRegistroDiarioDAO;
import pe.partnertech.fenosys.service.IRegistroDiarioService;

import javax.transaction.Transactional;

@Service
@Transactional
public class RegistroDiarioServiceImpl implements IRegistroDiarioService {

    @Autowired
    IRegistroDiarioDAO data;

    @Override
    public int GuardarRegistroDiario(RegistroDiario registrodiario) {

        int rpta = 0;

        RegistroDiario rd = data.save(registrodiario);

        if (!rd.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }
}
