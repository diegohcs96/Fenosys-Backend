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
import pe.partnertech.fenosys.service.IUbicacionService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UbicacionController {

    @Autowired
    IUbicacionService ubicacionService;

    @GetMapping("/ubicacion/show/paises")
    public ResponseEntity<?> MostrarPaises() {

        Set<PaisResponse> lista_paises = new HashSet<>();

        ubicacionService.ListaPaises().forEach(
                ubicacion -> {
                    lista_paises.add(
                            new PaisResponse(
                                    ubicacion.getPaisUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_paises, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/departamentos")
    public ResponseEntity<?> MostrarDepartamentos() {

        Set<DepartamentoResponse> lista_departamentos = new HashSet<>();

        ubicacionService.ListaDepartamentos().forEach(
                ubicacion -> {
                    lista_departamentos.add(
                            new DepartamentoResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_departamentos, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/provincias")
    public ResponseEntity<?> MostrarProvincias() {

        Set<ProvinciaResponse> lista_provincias = new HashSet<>();

        ubicacionService.ListaProvincias().forEach(
                ubicacion -> {
                    lista_provincias.add(
                            new ProvinciaResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion(),
                                    ubicacion.getProvinciaUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_provincias, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/distritos")
    public ResponseEntity<?> MostrarDistritos() {

        Set<DistritoResponse> lista_distritos = new HashSet<>();

        ubicacionService.ListaDistritos().forEach(
                ubicacion -> {
                    lista_distritos.add(
                            new DistritoResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion(),
                                    ubicacion.getProvinciaUbicacion(),
                                    ubicacion.getDistritoUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_distritos, HttpStatus.OK);
    }
}
