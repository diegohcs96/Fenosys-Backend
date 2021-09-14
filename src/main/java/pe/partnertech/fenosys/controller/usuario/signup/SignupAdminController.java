/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.signup;

import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.dto.request.restore_password.RestorePasswordRequest;
import pe.partnertech.fenosys.dto.request.usuario.signup.SignupAdminRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.enums.RolNombre;
import pe.partnertech.fenosys.model.*;
import pe.partnertech.fenosys.service.*;
import pe.partnertech.fenosys.util.UtilityFenologia;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupAdminController {

    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    IRestoreTokenService restoreTokenService;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    IDistritoService distritoService;
    @Autowired
    IRolService rolService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IImagenService imagenService;
    @Value("${front.baseurl}")
    private String baseurl;

    @PostMapping("/admin/signup_request")
    @PreAuthorize("hasRole('ROLE_FENOSIS')")
    public ResponseEntity<?> SignupAdminRequest(@RequestBody RestorePasswordRequest restorePasswordRequest,
                                                HttpServletRequest request) {

        if (usuarioService.ValidarEmail(restorePasswordRequest.getEmailUsuario())) {
            return new ResponseEntity<>(new MessageResponse("Ya se solicitó un proceso de Registro con ese Email."), HttpStatus.BAD_REQUEST);
        } else {
            String token = RandomString.make(50);

            Usuario new_admin = new Usuario();
            new_admin.setEmailUsuario(restorePasswordRequest.getEmailUsuario());
            usuarioService.GuardarUsuarioSemiFull(new_admin);

            Optional<Usuario> admin_data = usuarioService.BuscarUsuario_Email(restorePasswordRequest.getEmailUsuario());

            if (admin_data.isPresent()) {
                try {
                    Usuario admin = admin_data.get();

                    String identity1_admin = RandomString.make(2);
                    String identity2_admin = RandomString.make(3);
                    String username = "admin_" + identity1_admin + admin.getIdUsuario() + identity2_admin;

                    admin.setUsernameUsuario(username);
                    usuarioService.GuardarUsuarioSemiFull(admin);

                    RestoreToken restoreToken = new RestoreToken(
                            token,
                            "Signup Admin",
                            LocalDateTime.now().plusHours(72)
                    );

                    restoreTokenService.GuardarRestoreToken(restoreToken);

                    Set<RestoreToken> restoretoken_list;
                    if (admin.getRestoretokenUsuario() == null) {
                        restoretoken_list = new HashSet<>();
                    } else {
                        restoretoken_list = admin.getRestoretokenUsuario();
                    }
                    restoretoken_list.add(restoreToken);
                    admin.setRestoretokenUsuario(restoretoken_list);

                    usuarioService.GuardarUsuarioSemiFull(admin);

                    String url = UtilityFenologia.GenerarUrl(request) + "/api/admin_register_gateway?token=" + token;

                    EnviarCorreo(restorePasswordRequest.getEmailUsuario(), url);
                } catch (UnsupportedEncodingException e) {
                    return new ResponseEntity<>(new MessageResponse("Error: " + e), HttpStatus.BAD_REQUEST);
                } catch (MessagingException e) {
                    return new ResponseEntity<>(new MessageResponse("Error al enviar el correo."), HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>(new MessageResponse("Se envió el correo a la bandeja de entrada del " +
                        "Solicitante de manera satisfactoria."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error en la solicitud de Registro."), HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/admin_register_gateway")
    void RedirectSignupAdminRequest(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<RestoreToken> restoretoken_data = restoreTokenService.BuscarRestoreToken_Token(token);

        if (restoretoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/admin/" + token);
        } else {
            response.sendRedirect(baseurl);
        }
    }

    @PutMapping("/admin/signup")
    public ResponseEntity<?> SignupAdminProcess(@RequestPart("usuario") SignupAdminRequest signupAdminRequest,
                                                @RequestPart("foto") MultipartFile foto) {

        Optional<RestoreToken> restoretoken_data = restoreTokenService.BuscarRestoreToken_Token(signupAdminRequest.getRestoretokenUsuario());

        if (restoretoken_data.isPresent()) {
            RestoreToken restoretoken = restoretoken_data.get();

            Optional<Usuario> admin_data = usuarioService.BuscarUsuario_RestoreToken(restoretoken);

            if (admin_data.isPresent()) {
                Optional<Distrito> distrito_data = distritoService.BuscarDistrito_NombreDistrito(
                        signupAdminRequest.getDistritoUsuario()
                );

                if (distrito_data.isPresent()) {
                    Distrito distrito = distrito_data.get();
                    Usuario admin = admin_data.get();

                    admin.setNombreUsuario(signupAdminRequest.getNombreUsuario());
                    admin.setApellidoUsuario(signupAdminRequest.getApellidoUsuario());
                    admin.setPasswordUsuario(passwordEncoder.encode(signupAdminRequest.getPasswordUsuario()));
                    admin.setDistritoUsuario(distrito);

                    //Asignando Rol: Administrador
                    Optional<Rol> rol_data = rolService.BuscarRol_Nombre(RolNombre.ROLE_ADMIN);

                    if (rol_data.isPresent()) {
                        Set<Rol> roles = new HashSet<>();
                        roles.add(rol_data.get());
                        admin.setRolUsuario(roles);
                    } else {
                        return new ResponseEntity<>(new MessageResponse("Ocurrió un error al otorgar sus permisos correspondientes."), HttpStatus.NOT_FOUND);
                    }

                    //Asignando Fecha de Registro Actual
                    admin.setFecharegistroUsuario(LocalDate.now());

                    //Asignando Imagen
                    try {
                        String separadorfoto = Pattern.quote(".");
                        String[] formatofoto = foto.getOriginalFilename().split(separadorfoto);
                        String nombrefoto = UUID.randomUUID() + admin.getIdUsuario().toString() + UUID.randomUUID()
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
                            admin.setImagenUsuario(imagen);
                        } else {
                            InputStream fotoStream = getClass().getResourceAsStream("/AdminUser.png");
                            byte[] fotofile = IOUtils.toByteArray(fotoStream);

                            Imagen imagen = new Imagen(
                                    nombrefoto + "png",
                                    "image/png",
                                    urlfoto + "png",
                                    fotofile
                            );

                            imagenService.GuardarImagen(imagen);
                            admin.setImagenUsuario(imagen);
                        }
                    } catch (Exception e) {
                        return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e), HttpStatus.EXPECTATION_FAILED);
                    }

                    restoreTokenService.EliminarRestoreToken_MiddleTable(restoretoken.getIdRestoreToken());
                    restoreTokenService.EliminarRestoreToken_This(restoretoken.getIdRestoreToken());

                    usuarioService.GuardarUsuarioFull(admin, foto);

                    return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente."), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrio un error al buscar su Ubicación."), HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar su Ubicación."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error durante el proceso de Registro."), HttpStatus.NOT_FOUND);
        }
    }

    private void EnviarCorreo(String email, String url) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("fenosis.assistant@gmail.com", "Fenosis Support");
        helper.setTo(email);

        String asunto = "Solicitud de Registro de Administrador";

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_Email(email);

        Usuario admin = admin_data.get();

        String contenido =
                "<h2>Hola,</h1>" +
                        "<p>Gracias por realizar tu solicitud de Registro de Usuario.</p>" +
                        "<p>Hemos generado un usuario para continuar con el trámite. Del mismo modo, debes acceder al link para " +
                        "culminar con el registro para acceder al sistema.</p>" +
                        "<br>" +
                        "<h4>Usuario: </h2>" + admin.getUsernameUsuario() +
                        "<br>" +
                        "<br>Haz click en el link que se encuentra debajo para continuar con el registro." +
                        "<a href=" + url + ">Completar mi registro</a>";

        helper.setSubject(asunto);
        helper.setText(contenido, true);

        mailSender.send(message);
    }
}
