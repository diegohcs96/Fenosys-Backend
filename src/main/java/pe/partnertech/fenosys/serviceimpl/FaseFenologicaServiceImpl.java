/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.FaseFenologica;
import pe.partnertech.fenosys.repository.IFaseFenologicaDAO;
import pe.partnertech.fenosys.service.IFaseFenologicaService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class FaseFenologicaServiceImpl implements IFaseFenologicaService {

    @Autowired
    IFaseFenologicaDAO data;

    @Override
    public Set<FaseFenologica> BuscarFasesFenologicas_From_FFPC_MiddleTable_By_IDPlanillaCosecha(Long id) {
        return data.BuscarFasesFenologicas_From_FFPC_MiddleTable_By_IDPlanillaCosecha(id);
    }

    @Override
    public int GuardarFasesFenologicas_Config(List<FaseFenologica> lista_fasesfenologicas) {
        int rpta = 0;

        List<FaseFenologica> ff = data.saveAll(lista_fasesfenologicas);

        if (!ff.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public void EliminarFaseFenologica_From_FFPC_MiddleTable_By_IDPlanillaCosecha(Long id) {
        data.EliminarFaseFenologica_From_FFPC_MiddleTable_By_IDPlanillaCosecha(id);
    }

    @Override
    public void EliminarFaseFenologica(Long id) {
        data.deleteById(id);
    }
}
