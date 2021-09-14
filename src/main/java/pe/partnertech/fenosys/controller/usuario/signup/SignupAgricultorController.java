/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.signup;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.dto.request.usuario.signup.SignupAgricultorRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.enums.RolNombre;
import pe.partnertech.fenosys.model.Distrito;
import pe.partnertech.fenosys.model.Imagen;
import pe.partnertech.fenosys.model.Rol;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IDistritoService;
import pe.partnertech.fenosys.service.IImagenService;
import pe.partnertech.fenosys.service.IRolService;
import pe.partnertech.fenosys.service.IUsuarioService;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupAgricultorController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IRolService rolService;

    @Autowired
    IImagenService imagenService;

    @Autowired
    IDistritoService distritoService;

    @PostMapping("/agricultor/signup")
    public ResponseEntity<?> SignUpPostulante(@RequestPart("usuario") SignupAgricultorRequest signupAgricultorRequest,
                                              @RequestPart("foto") MultipartFile foto) {

        if (usuarioService.ValidarUsername(signupAgricultorRequest.getUsernameUsuario())) {
            return new ResponseEntity<>(new MessageResponse("El Usuario ya se encuentra en uso."), HttpStatus.BAD_REQUEST);
        } else if (usuarioService.ValidarEmail(signupAgricultorRequest.getEmailUsuario())) {
            return new ResponseEntity<>(new MessageResponse("El Email ya se encuentra en uso."), HttpStatus.BAD_REQUEST);
        } else {
            Optional<Distrito> distrito_data = distritoService.BuscarDistrito_NombreDistrito(signupAgricultorRequest.getDistritoUsuario());

            if (distrito_data.isPresent()) {
                Distrito distrito = distrito_data.get();

                Usuario agricultor =
                        new Usuario(
                                signupAgricultorRequest.getNombreUsuario(),
                                signupAgricultorRequest.getApellidoUsuario(),
                                signupAgricultorRequest.getEmailUsuario(),
                                signupAgricultorRequest.getUsernameUsuario(),
                                passwordEncoder.encode(signupAgricultorRequest.getPasswordUsuario()),
                                distrito
                        );

                //Asignando Rol: Agricultor
                Optional<Rol> rol_data = rolService.BuscarRol_Nombre(RolNombre.ROLE_AGRICULTOR);

                if (rol_data.isPresent()) {
                    Set<Rol> roles = new HashSet<>();
                    roles.add(rol_data.get());
                    agricultor.setRolUsuario(roles);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al otorgar sus permisos correspondientes."), HttpStatus.NOT_FOUND);
                }

                //Asignando Fecha de Registro Actual
                agricultor.setFecharegistroUsuario(LocalDate.now());

                //Asignando Imagen
                try {
                    String separadorfoto = Pattern.quote(".");
                    String[] formatofoto = foto.getOriginalFilename().split(separadorfoto);
                    String nombrefoto = UUID.randomUUID() + "" + UUID.randomUUID()
                            + "." + formatofoto[formatofoto.length - 1];

                    String urlfoto = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/photos/")
                            .path(nombrefoto)
                            .toUriString();

                    if (!foto.isEmpty()) {
                        Imagen imagen = new Imagen(
                                nombrefoto,
                                foto.getContentType(),
                                urlfoto,
                                foto.getBytes()
                        );

                        imagenService.GuardarImagen(imagen);
                        agricultor.setImagenUsuario(imagen);
                    } else {
                        InputStream fotoStream = getClass().getResourceAsStream("/AgroUser.png");
                        byte[] fotofile = IOUtils.toByteArray(fotoStream);

                        Imagen imagen = new Imagen(
                                nombrefoto + "png",
                                "image/png",
                                urlfoto + "png",
                                fotofile
                        );

                        imagenService.GuardarImagen(imagen);
                        agricultor.setImagenUsuario(imagen);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e), HttpStatus.EXPECTATION_FAILED);
                }

                usuarioService.GuardarUsuarioFull(agricultor, foto);

                return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar su Ubicación."), HttpStatus.NOT_FOUND);
            }
        }
    }
}
