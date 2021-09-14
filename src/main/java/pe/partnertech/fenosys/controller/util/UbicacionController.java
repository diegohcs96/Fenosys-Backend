/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.partnertech.fenosys.dto.response.general.ubicacion.DepartamentoResponse;
import pe.partnertech.fenosys.dto.response.general.ubicacion.DistritoResponse;
import pe.partnertech.fenosys.dto.response.general.ubicacion.PaisResponse;
import pe.partnertech.fenosys.dto.response.general.ubicacion.ProvinciaResponse;
import pe.partnertech.fenosys.model.Departamento;
import pe.partnertech.fenosys.model.Pais;
import pe.partnertech.fenosys.model.Provincia;
import pe.partnertech.fenosys.service.IDepartamentoService;
import pe.partnertech.fenosys.service.IDistritoService;
import pe.partnertech.fenosys.service.IPaisService;
import pe.partnertech.fenosys.service.IProvinciaService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UbicacionController {

    @Autowired
    IPaisService paisService;

    @Autowired
    IDepartamentoService departamentoService;

    @Autowired
    IProvinciaService provinciaService;

    @Autowired
    IDistritoService distritoService;

    @GetMapping("/ubicacion/show/paises")
    public ResponseEntity<?> MostrarPaises() {

        Set<PaisResponse> lista_paises = new HashSet<>();

        paisService.MostrarPaises().forEach(
                pais -> {
                    lista_paises.add(
                            new PaisResponse(
                                    pais.getIdPais(),
                                    pais.getNombrePais()
                            ));
                });

        return new ResponseEntity<>(lista_paises, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/departamentos")
    public ResponseEntity<?> MostrarDepartamentos() {

        Set<DepartamentoResponse> lista_departamentos = new HashSet<>();

        departamentoService.MostrarDepartamentos().forEach(
                departamento -> {
                    lista_departamentos.add(
                            new DepartamentoResponse(
                                    BuscarPaisByDepartamento(departamento.getIdDepartamento()),
                                    departamento.getIdDepartamento(),
                                    departamento.getNombreDepartamento()
                            ));
                });

        return new ResponseEntity<>(lista_departamentos, HttpStatus.OK);
    }

    Long BuscarPaisByDepartamento(Long iddepartamento) {

        Optional<Pais> pais_data = paisService.BuscarPais_IDDepartamento(iddepartamento);

        if (pais_data.isPresent()) {
            Pais pais = pais_data.get();

            return pais.getIdPais();
        } else {
            return null;
        }
    }

    @GetMapping("/ubicacion/show/provincias")
    public ResponseEntity<?> MostrarProvincias() {

        Set<ProvinciaResponse> lista_provincias = new HashSet<>();

        provinciaService.MostrarProvincias().forEach(
                provincia -> {
                    lista_provincias.add(
                            new ProvinciaResponse(
                                    BuscarDepartamentoByProvincia(provincia.getIdProvincia()),
                                    provincia.getIdProvincia(),
                                    provincia.getNombreProvincia()
                            ));
                });

        return new ResponseEntity<>(lista_provincias, HttpStatus.OK);
    }

    Long BuscarDepartamentoByProvincia(Long idprovincia) {

        Optional<Departamento> departamento_data = departamentoService.BuscarDepartamento_IDProvincia(idprovincia);

        if (departamento_data.isPresent()) {
            Departamento departamento = departamento_data.get();

            return departamento.getIdDepartamento();
        } else {
            return null;
        }
    }

    @GetMapping("/ubicacion/show/distritos")
    public ResponseEntity<?> MostrarDistritos() {

        Set<DistritoResponse> lista_distritos = new HashSet<>();

        distritoService.MostrarDistritos().forEach(
                distrito -> {
                    lista_distritos.add(
                            new DistritoResponse(
                                    BuscarProvinciaByDistrito(distrito.getIdDistrito()),
                                    distrito.getIdDistrito(),
                                    distrito.getNombreDistrito()
                            ));
                });

        return new ResponseEntity<>(lista_distritos, HttpStatus.OK);
    }

    Long BuscarProvinciaByDistrito(Long iddistrito) {

        Optional<Provincia> provincia_data = provinciaService.BuscarProvincia_IDDistrito(iddistrito);

        if (provincia_data.isPresent()) {
            Provincia provincia = provincia_data.get();

            return provincia.getIdProvincia();
        } else {
            return null;
        }
    }
}
