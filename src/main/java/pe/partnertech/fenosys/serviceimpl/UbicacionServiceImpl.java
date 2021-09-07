/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Ubicacion;
import pe.partnertech.fenosys.repository.IUbicacionDAO;
import pe.partnertech.fenosys.service.IUbicacionService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UbicacionServiceImpl implements IUbicacionService {

    @Autowired
    IUbicacionDAO data;

    @Override
    public Optional<Ubicacion> BuscarUbicacion_PaisyDepartamentoyProvinciayDistrito(String pais, String departamento, String provincia, String distrito) {
        return data.findByPaisUbicacionAndDepartamentoUbicacionAndProvinciaUbicacionAndDistritoUbicacion(pais, departamento,
                provincia, distrito);
    }

    @Override
    public Boolean ValidarUbicacion(String pais, String departamento, String provincia, String distrito) {
        return data.existsByPaisUbicacionAndDepartamentoUbicacionAndProvinciaUbicacionAndDistritoUbicacion(pais,
                departamento, provincia, distrito);
    }

    @Override
    public int GuardarUbicacion(Ubicacion ubicacion) {
        int rpta = 0;

        Ubicacion u = data.save(ubicacion);

        if (!u.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public Set<Ubicacion> ListaUbicaciones() {
        return data.findAllUbicaciones();
    }
}
