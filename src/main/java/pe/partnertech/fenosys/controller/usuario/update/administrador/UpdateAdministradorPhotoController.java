/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.update.administrador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IImagenService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UpdateAdministradorPhotoController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IImagenService imagenService;

    @PutMapping("/admin/{id}/update/foto")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> UpdateFotoAdministrador(@PathVariable("id") Long id, @RequestPart("foto") MultipartFile foto) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_ID(id);

        if (admin_data.isPresent()) {
            Usuario admin = admin_data.get();

            Optional<Imagen> imagen_data = imagenService.BuscarImagen_ID(admin.getImagenUsuario().getIdImagen());

            if (imagen_data.isPresent()) {
                try {
                    Imagen imagen = imagen_data.get();

                    String separadorfoto = Pattern.quote(".");
                    String[] formatofoto = foto.getOriginalFilename().split(separadorfoto);

                    String nombrefoto = UUID.randomUUID() + admin.getIdUsuario().toString() +
                            UUID.randomUUID() + "." + formatofoto[formatofoto.length - 1];

                    String urlfoto = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/photos/")
                            .path(nombrefoto)
                            .toUriString();

                    if (!foto.isEmpty()) {
                        imagen.setNombreImagen(nombrefoto);
                        imagen.setTipoarchivoImagen(foto.getContentType());
                        imagen.setUrlImagen(urlfoto);
                        imagen.setArchivoImagen(foto.getBytes());

                        imagenService.GuardarImagen(imagen);
                        admin.setImagenUsuario(imagen);
                        usuarioService.GuardarUsuarioFull(admin, foto);

                        return new ResponseEntity<>(new MessageResponse("Se ha actualizado su foto de perfil satisfactoriamente."), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new MessageResponse("No se ha seleccionado un archivo"), HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e), HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("No se encontró la información requerida."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encontró información del usuario."), HttpStatus.NOT_FOUND);
        }
    }
}
