/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.Pais;
import pe.partnertech.fenosys.repository.IPaisDAO;
import pe.partnertech.fenosys.service.IPaisService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PaisServiceImpl implements IPaisService {

    @Autowired
    IPaisDAO data;

    @Override
    public int GuardarPais(Pais pais) {
        int rpta = 0;

        Pais p = data.save(pais);

        if (!p.equals(null)) {

            rpta = 1;
        }

        return rpta;
    }

    @Override
    public Set<Pais> MostrarPaises() {
        return data.findAllPaises();
    }

    @Override
    public Optional<Pais> BuscarPais_IDDepartamento(Long id) {
        return data.findPaisByIDDepartamento(id);
    }

    @Override
    public Optional<Pais> BuscarPais_Pais(String pais) {
        return data.findByNombrePais(pais);
    }

    @Override
    public boolean ValidarNombrePais(String pais) {
        return data.existsByNombrePais(pais);
    }

    @Override
    public void EliminarPais(Long id) {
        data.deleteById(id);
    }
}
