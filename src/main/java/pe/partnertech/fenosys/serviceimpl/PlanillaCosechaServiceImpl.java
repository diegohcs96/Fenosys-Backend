/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.PlanillaCosecha;
import pe.partnertech.fenosys.repository.IPlanillaCosechaDAO;
import pe.partnertech.fenosys.service.IPlanillaCosechaService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PlanillaCosechaServiceImpl implements IPlanillaCosechaService {

    @Autowired
    IPlanillaCosechaDAO data;

    @Override
    public Optional<PlanillaCosecha> BuscarPlanillaCosecha_ID(Long id) {
        return data.findById(id);
    }

    @Override
    public Optional<PlanillaCosecha> BuscarPlanillaCosecha_CosechaYIDAgricultor(String nombrecosecha, Long idagricultor) {
        return data.findByNombreCosechaYIDAgricultor(nombrecosecha, idagricultor);
    }

    @Override
    public int GuardarPlanillaCosecha(PlanillaCosecha planillacosecha) {
        int rpta = 0;

        PlanillaCosecha pc = data.save(planillacosecha);

        if (!pc.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public void EliminarPlanillaCosecha_From_PCU_MiddleTable(Long id) {
        data.deletePlanillaCosechaFromPCUMiddleTable(id);
    }

    @Override
    public void EliminarPlanillaCosecha_This(Long id) {
        data.deleteById(id);
    }
}
