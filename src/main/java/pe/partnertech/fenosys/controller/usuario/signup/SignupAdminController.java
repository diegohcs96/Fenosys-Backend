/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.signup;

import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.IOUtils;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_SetUserRol;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_SignupValidations;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_UploadFoto;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_UtilityToken;
import pe.partnertech.fenosys.dto.request.usuario.general.EmailRequest;
import pe.partnertech.fenosys.dto.request.usuario.signup.SignupAdminRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.enums.RolNombre;
import pe.partnertech.fenosys.model.*;
import pe.partnertech.fenosys.service.*;
import pe.partnertech.fenosys.tools.UtilityFenosys;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupAdminController {

    final
    IUsuarioService usuarioService;
    final
    IUtilityTokenService utilityTokenService;
    final
    JavaMailSender mailSender;
    final
    IDistritoService distritoService;
    final
    IRolService rolService;
    final
    PasswordEncoder passwordEncoder;
    final
    IImagenService imagenService;
    final
    TemplateEngine templateEngine;
    Code_SignupValidations code_signupValidations;
    Code_UploadFoto code_uploadFoto;
    @Value("${front.baseurl}")
    private String baseurl;
    @Value("${image.mail.url}")
    private String logomail_url;

    public SignupAdminController(IUsuarioService usuarioService, IUtilityTokenService utilityTokenService,
                                 JavaMailSender mailSender, IDistritoService distritoService, IRolService rolService,
                                 PasswordEncoder passwordEncoder, IImagenService imagenService,
                                 TemplateEngine templateEngine) {
        this.usuarioService = usuarioService;
        this.utilityTokenService = utilityTokenService;
        this.mailSender = mailSender;
        this.distritoService = distritoService;
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
        this.imagenService = imagenService;
        this.templateEngine = templateEngine;
    }

    @PostMapping("/admin/signup_request")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    public ResponseEntity<?> SignupAdminRequest(@RequestBody EmailRequest emailRequest,
                                                HttpServletRequest request) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_EmailUsuario(emailRequest.getEmailUsuario());

        if (usuario_data.isPresent()) {
            return code_signupValidations.SignupValidation(usuario_data);
        } else {
            String token = RandomString.make(50);

            Usuario new_admin = new Usuario();
            new_admin.setEmailUsuario(emailRequest.getEmailUsuario());
            new_admin.setEstadoUsuario("PENDIENTE");
            usuarioService.GuardarUsuario(new_admin);

            Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_EmailUsuario(emailRequest.getEmailUsuario());

            if (admin_data.isPresent()) {
                try {
                    Usuario admin = admin_data.get();

                    String identity1_admin = RandomString.make(2);
                    String identity2_admin = RandomString.make(3);
                    String username = "admin_" + identity1_admin + admin.getIdUsuario() + identity2_admin;

                    admin.setUsernameUsuario(username);
                    usuarioService.GuardarUsuario(admin);

                    UtilityToken utilityToken = new UtilityToken(
                            token,
                            "Signup Admin",
                            LocalDateTime.now().plusHours(72)
                    );

                    Code_UtilityToken.UtilityTokenUser(admin, utilityToken, utilityTokenService, usuarioService);

                    String url = UtilityFenosys.GenerarUrl(request) + "/api/admin_register_gateway?token=" + token;

                    EnviarCorreo(emailRequest.getEmailUsuario(), url);
                } catch (UnsupportedEncodingException e) {
                    return new ResponseEntity<>(new MessageResponse("Error: " + e), HttpStatus.BAD_REQUEST);
                } catch (MessagingException e) {
                    return new ResponseEntity<>(new MessageResponse("Error al enviar el correo."),
                            HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>(new MessageResponse("Se envió el correo a la bandeja de entrada del " +
                        "Solicitante de manera satisfactoria."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error en la solicitud de Registro."),
                        HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping("/admin_register_gateway")
    void RedirectSignupAdminRequest(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/admin/" + token);
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PutMapping("/admin/signup")
    public ResponseEntity<?> SignupAdminProcess(@RequestPart("usuario") SignupAdminRequest signupAdminRequest,
                                                @RequestPart("foto") MultipartFile foto) {

        Optional<UtilityToken> utilitytoken_data =
                utilityTokenService.BuscarUtilityToken_By_Token(signupAdminRequest.getUtilitytokenUsuario());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_UtilityToken(utilitytoken);

            if (admin_data.isPresent()) {
                Optional<Distrito> distrito_data = distritoService.BuscarDistrito_By_IDDistrito(
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

                    if (Code_SetUserRol.SetUserRol(admin, rol_data))
                        return new ResponseEntity<>(new MessageResponse("Ocurrió un error al otorgar sus permisos correspondientes."),
                                HttpStatus.NOT_FOUND);

                    //Asignando Fecha de Registro Actual
                    admin.setFecharegistroUsuario(LocalDate.now());

                    //Cambiando Estado de Cuenta a ACTIVO
                    admin.setEstadoUsuario("ACTIVO");

                    //Asignando Imagen
                    try {
                        if (!foto.isEmpty()) {
                            Imagen imagen = new Imagen(
                                    code_uploadFoto.getNombreFoto(),
                                    foto.getContentType(),
                                    code_uploadFoto.getUrlFoto(),
                                    foto.getBytes()
                            );

                            imagenService.GuardarImagen(imagen);
                            admin.setImagenUsuario(imagen);
                        } else {
                            InputStream fotoStream = getClass().getResourceAsStream("/static/img/AdminUser.png");
                            assert fotoStream != null;
                            byte[] file_foto = IOUtils.toByteArray(fotoStream);

                            Imagen imagen = new Imagen(
                                    code_uploadFoto.getNombreFoto() + "png",
                                    "image/png",
                                    code_uploadFoto.getUrlFoto() + "png",
                                    file_foto
                            );

                            imagenService.GuardarImagen(imagen);
                            admin.setImagenUsuario(imagen);
                        }
                    } catch (Exception e) {
                        return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e),
                                HttpStatus.EXPECTATION_FAILED);
                    }

                    utilityTokenService.EliminarUtilityToken_MiddleTable(utilitytoken.getIdUtilityToken());
                    utilityTokenService.EliminarUtilityToken_This(utilitytoken.getIdUtilityToken());

                    usuarioService.GuardarUsuarioMultipart(admin, foto);

                    return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente."),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar su Ubicación."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error durante el proceso de Registro."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("El proceso de registro ya no se encuentra disponible."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private void EnviarCorreo(String email, String url) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("fenosys.assistant@gmail.com", "Fenosys Support");
        helper.setTo(email);

        String asunto = "Solicitud de Registro de Administrador";

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_EmailUsuario(email);

        if (admin_data.isPresent()) {
            Usuario admin = admin_data.get();

            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("username", admin.getUsernameUsuario());
            model.put("url", url);
            model.put("logomail_Url", logomail_url);
            model.put("frontend_baseUrl", baseurl);

            context.setVariables(model);

            String html_template = templateEngine.process("adminrequest-mailtemplate", context);

            helper.setSubject(asunto);
            helper.setText(html_template, true);

            mailSender.send(message);
        }
    }
}
