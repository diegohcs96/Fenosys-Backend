/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.profile.master;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.dto.response.general.ImagenResponse;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.dto.response.profile.BasicInfoUserResponse;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IImagenService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BasicInfoMasterController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IImagenService imagenService;

    @GetMapping("/master/{id}/basicinfo")
    @PreAuthorize("hasRole('ROLE_FENOSIS')")
    public ResponseEntity<?> MasterProfile(@PathVariable("id") Long id) {

        Optional<Usuario> master_data = usuarioService.BuscarUsuario_ID(id);

        if (master_data.isPresent()) {
            Usuario master = master_data.get();

            if (master.getImagenUsuario().getNombreImagen().equals("") && master.getImagenUsuario().getTipoarchivoImagen().equals("")) {
                try {
                    Imagen foto = master.getImagenUsuario();

                    String urlfoto = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/photos/")
                            .path(foto.getNombreImagen())
                            .toUriString();

                    InputStream fotoStream = getClass().getResourceAsStream("/images/MasterUser.png");
                    byte[] fotofile = IOUtils.toByteArray(fotoStream);

                    foto.setUrlImagen(urlfoto);
                    foto.setArchivoImagen(fotofile);

                    imagenService.GuardarImagen(foto);

                    master.setImagenUsuario(foto);
                    usuarioService.GuardarUsuarioSemiFull(master);

                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("Error al cargar la imagen " + e), HttpStatus.EXPECTATION_FAILED);
                }

                Optional<Imagen> imagen_data = imagenService.BuscarImagen_ID(master.getImagenUsuario().getIdImagen());

                if (imagen_data.isPresent()) {
                    Imagen foto = imagen_data.get();

                    return new ResponseEntity<>(new BasicInfoUserResponse(
                            master.getNombreUsuario(),
                            master.getApellidoUsuario(),
                            null,
                            null,
                            null,
                            null,
                            master.getEmailUsuario(),
                            new ImagenResponse(foto.getNombreImagen(), foto.getUrlImagen())
                    ), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("No se encuentra la foto de perfil del usuario."), HttpStatus.NOT_FOUND);
                }
            } else {
                Optional<Imagen> imagen_data = imagenService.BuscarImagen_ID(master.getImagenUsuario().getIdImagen());

                if (imagen_data.isPresent()) {
                    Imagen foto = imagen_data.get();

                    return new ResponseEntity<>(new BasicInfoUserResponse(
                            master.getNombreUsuario(),
                            master.getApellidoUsuario(),
                            null,
                            null,
                            null,
                            null,
                            master.getEmailUsuario(),
                            new ImagenResponse(foto.getNombreImagen(), foto.getUrlImagen())
                    ), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("No se encuentra la foto de perfil del usuario."), HttpStatus.NOT_FOUND);
                }
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encuentra información del perfil del usuario."), HttpStatus.NOT_FOUND);
        }
    }
}
