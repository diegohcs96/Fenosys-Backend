/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.update.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.request.usuario.UpdateBasicInfoRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.Ubicacion;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IUbicacionService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UpdateAdministradorBasicInfoController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IUbicacionService ubicacionService;

    @PutMapping("/admin/{id}/update/basicinfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> UpdateBasicInfoAdministrador(@PathVariable("id") Long id,
                                                          @RequestBody UpdateBasicInfoRequest updateBasicInfoRequest) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_ID(id);

        if (admin_data.isPresent()) {
            Optional<Ubicacion> ubicacion_data = ubicacionService.BuscarUbicacion_PaisyDepartamentoyProvinciayDistrito(
                    updateBasicInfoRequest.getPaisUsuario(), updateBasicInfoRequest.getDepartamentoUsuario(),
                    updateBasicInfoRequest.getProvinciaUsuario(), updateBasicInfoRequest.getDistritoUsuario()
            );

            if (ubicacion_data.isPresent()) {
                Usuario admin = admin_data.get();

                Ubicacion ubicacion = ubicacion_data.get();

                admin.setNombreUsuario(updateBasicInfoRequest.getNombreUsuario());
                admin.setApellidoUsuario(updateBasicInfoRequest.getApellidoUsuario());
                admin.setUbicacionUsuario(ubicacion);

                usuarioService.GuardarUsuarioSemiFull(admin);

                return new ResponseEntity<>(new MessageResponse("Se ha actualizado sus datos satisfactoriamente."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar su Ubicación."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encontro información del usuario."), HttpStatus.NOT_FOUND);
        }
    }
}
