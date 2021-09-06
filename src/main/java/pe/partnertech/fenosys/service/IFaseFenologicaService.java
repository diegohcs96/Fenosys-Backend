/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.FaseFenologica;

import java.util.List;
import java.util.Set;

public interface IFaseFenologicaService {

    Set<FaseFenologica> BuscarFasesFenologicas_From_FFPC_MiddleTable_By_IDPlanillaCosecha(Long id);

    int GuardarFasesFenologicas_Config(List<FaseFenologica> lista_fasesfenologicas);

    void EliminarFaseFenologica_From_FFPC_MiddleTable_By_IDPlanillaCosecha(Long id);

    void EliminarFaseFenologica(Long id);
}
