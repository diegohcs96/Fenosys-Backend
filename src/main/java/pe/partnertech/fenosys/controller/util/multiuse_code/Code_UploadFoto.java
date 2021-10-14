/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.util.multiuse_code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IImagenService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

public class Code_UploadFoto {

    //Atributos
    String nombreFoto;
    String urlFoto;
    //Constructores
    public Code_UploadFoto(String nombreFoto, String urlFoto) {
        this.nombreFoto = nombreFoto;
        this.urlFoto = urlFoto;
    }

    public static Code_UploadFoto AssignFoto(MultipartFile foto, Usuario usuario) {
        String separador_foto = Pattern.quote(".");
        String[] formato_foto = Objects.requireNonNull(foto.getOriginalFilename()).split(separador_foto);

        String nombre_foto = UUID.randomUUID() + usuario.getIdUsuario().toString() +
                UUID.randomUUID() + "." + formato_foto[formato_foto.length - 1];

        String url_foto = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/photos/")
                .path(nombre_foto)
                .toUriString();

        return new Code_UploadFoto(
                nombre_foto,
                url_foto
        );
    }

    public ResponseEntity<?> UpdateFoto(Long id_usuario, @RequestPart("foto") MultipartFile foto,
                                        IUsuarioService usuarioService, IImagenService imagenService) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_IDUsuario(id_usuario);

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            Optional<Imagen> imagen_data = imagenService.BuscarImagen_ID(usuario.getImagenUsuario().getIdImagen());

            if (imagen_data.isPresent()) {
                try {
                    Imagen imagen = imagen_data.get();

                    if (!foto.isEmpty()) {
                        imagen.setNombreImagen(AssignFoto(foto, usuario).nombreFoto);
                        imagen.setTipoarchivoImagen(foto.getContentType());
                        imagen.setUrlImagen(AssignFoto(foto, usuario).urlFoto);
                        imagen.setArchivoImagen(foto.getBytes());

                        imagenService.GuardarImagen(imagen);
                        usuario.setImagenUsuario(imagen);
                        usuarioService.GuardarUsuarioMultipart(usuario, foto);

                        return new ResponseEntity<>(new MessageResponse("Se ha actualizado su foto de perfil satisfactoriamente."),
                                HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new MessageResponse("No se ha seleccionado un archivo"),
                                HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e),
                            HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("No se encontró la información requerida."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encontró información del usuario."),
                    HttpStatus.NOT_FOUND);
        }
    }

    //Getters y Setters
    public String getNombreFoto() {
        return nombreFoto;
    }

    public String getUrlFoto() {
        return urlFoto;
    }
}
