/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.mantenimiento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.Ubicacion;
import pe.partnertech.fenosys.service.IUbicacionService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UbicacionManagementController {

    @Autowired
    IUbicacionService ubicacionService;

    @PostMapping("/ubicacion/agregar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> Agregarubicacion(@RequestBody Ubicacion ubicacion) {
        if (ubicacionService.ValidarUbicacion(ubicacion.getPaisUbicacion(), ubicacion.getDepartamentoUbicacion(),
                ubicacion.getProvinciaUbicacion(), ubicacion.getDistritoUbicacion())) {
            return new ResponseEntity<>(new MessageResponse("Ya se encuentra registrada dicha ubicación."),
                    HttpStatus.BAD_REQUEST);
        } else {
            ubicacionService.GuardarUbicacion(ubicacion);

            return new ResponseEntity<>(new MessageResponse("Se ha guardado la ubicación satisfactoriamente."),
                    HttpStatus.OK);
        }
    }
}
