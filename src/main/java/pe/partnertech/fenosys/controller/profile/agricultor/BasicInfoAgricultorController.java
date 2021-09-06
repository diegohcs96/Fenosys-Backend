/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.profile.agricultor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.response.general.ImagenResponse;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.dto.response.profile.BasicInfoUserResponse;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IImagenService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BasicInfoAgricultorController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IImagenService imagenService;

    @GetMapping("/agricultor/{id}/basicinfo")
    @PreAuthorize("hasRole('ROLE_AGRICULTOR')")
    public ResponseEntity<?> AgricultorProfile(@PathVariable("id") Long id) {

        Optional<Usuario> agricultor_data = usuarioService.BuscarUsuario_ID(id);

        if (agricultor_data.isPresent()) {
            Usuario agricultor = agricultor_data.get();

            Optional<Imagen> imagen_data = imagenService.BuscarImagen_ID(agricultor.getImagenUsuario().getIdImagen());

            if (imagen_data.isPresent()) {
                Imagen foto = imagen_data.get();

                return new ResponseEntity<>(new BasicInfoUserResponse(
                        agricultor.getNombreUsuario(),
                        agricultor.getApellidoUsuario(),
                        agricultor.getUbicacionUsuario().getPaisUbicacion(),
                        agricultor.getUbicacionUsuario().getDepartamentoUbicacion(),
                        agricultor.getUbicacionUsuario().getProvinciaUbicacion(),
                        agricultor.getUbicacionUsuario().getDistritoUbicacion(),
                        agricultor.getEmailUsuario(),
                        new ImagenResponse(foto.getNombreImagen(), foto.getUrlImagen())
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("No se encuentra la foto de perfil del usuario."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encuentra información del perfil del usuario."), HttpStatus.NOT_FOUND);
        }
    }
}
