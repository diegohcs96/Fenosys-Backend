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
import pe.partnertech.fenosys.dto.response.general.UbicacionResponse;
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

        Set<UbicacionResponse> lista_paises = new HashSet<>();

        ubicacionService.ListaUbicaciones().forEach(
                ubicacion -> {
                    lista_paises.add(
                            new UbicacionResponse(
                                    ubicacion.getPaisUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_paises, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/departamentos")
    public ResponseEntity<?> MostrarDepartamentos() {

        Set<UbicacionResponse> lista_departamentos = new HashSet<>();

        ubicacionService.ListaUbicaciones().forEach(
                ubicacion -> {
                    lista_departamentos.add(
                            new UbicacionResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_departamentos, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/provincias")
    public ResponseEntity<?> MostrarProvincias() {

        Set<UbicacionResponse> lista_provincias = new HashSet<>();

        ubicacionService.ListaUbicaciones().forEach(
                ubicacion -> {
                    lista_provincias.add(
                            new UbicacionResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion(),
                                    ubicacion.getProvinciaUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_provincias, HttpStatus.OK);
    }

    @GetMapping("/ubicacion/show/distritos")
    public ResponseEntity<?> MostrarDistritos() {

        Set<UbicacionResponse> lista_distritos = new HashSet<>();

        ubicacionService.ListaUbicaciones().forEach(
                ubicacion -> {
                    lista_distritos.add(
                            new UbicacionResponse(
                                    ubicacion.getPaisUbicacion(),
                                    ubicacion.getDepartamentoUbicacion(),
                                    ubicacion.getProvinciaUbicacion(),
                                    ubicacion.getDistritoUbicacion()
                            ));
                });

        return new ResponseEntity<>(lista_distritos, HttpStatus.OK);
    }
}
