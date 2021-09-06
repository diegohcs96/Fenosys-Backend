/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.PlanillaCosecha;

import java.util.Optional;

public interface IPlanillaCosechaService {

    Optional<PlanillaCosecha> BuscarPlanillaCosecha_ID(Long id);

    Optional<PlanillaCosecha> BuscarPlanillaCosecha_CosechaYIDAgricultor(String nombrecosecha, Long idagricultor);

    int GuardarPlanillaCosecha(PlanillaCosecha planillacosecha);

    void EliminarPlanillaCosecha_From_PCU_MiddleTable(Long id);

    void EliminarPlanillaCosecha_This(Long id);
}
