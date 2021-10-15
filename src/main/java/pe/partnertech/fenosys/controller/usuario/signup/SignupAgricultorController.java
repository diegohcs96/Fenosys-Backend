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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_SetUserRol;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_SignupValidations;
import pe.partnertech.fenosys.controller.util.multiuse_code.Code_UtilityToken;
import pe.partnertech.fenosys.dto.request.usuario.general.UtilityTokenRequest;
import pe.partnertech.fenosys.dto.request.usuario.signup.SignupAgricultorRequest;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupAgricultorController {

    final
    PasswordEncoder passwordEncoder;

    final
    IUsuarioService usuarioService;

    final
    IRolService rolService;

    final
    IImagenService imagenService;

    final
    IDistritoService distritoService;

    final
    JavaMailSender mailSender;

    final
    IUtilityTokenService utilityTokenService;
    Code_SignupValidations code_signupValidations;
    @Value("${front.baseurl}")
    private String baseurl;

    public SignupAgricultorController(PasswordEncoder passwordEncoder, IUsuarioService usuarioService,
                                      IRolService rolService, IImagenService imagenService,
                                      IDistritoService distritoService, JavaMailSender mailSender,
                                      IUtilityTokenService utilityTokenService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.imagenService = imagenService;
        this.distritoService = distritoService;
        this.mailSender = mailSender;
        this.utilityTokenService = utilityTokenService;
    }

    @PostMapping("/agricultor/signup")
    public ResponseEntity<?> SignUpPostulante(@RequestPart("usuario") SignupAgricultorRequest signupAgricultorRequest,
                                              @RequestPart("foto") MultipartFile foto,
                                              HttpServletRequest request) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_EmailUsuario(signupAgricultorRequest.getEmailUsuario());

        if (usuario_data.isPresent()) {
            return code_signupValidations.SignupValidation(usuario_data);
        } else {
            if (usuarioService.ValidarUsername(signupAgricultorRequest.getUsernameUsuario())) {
                return new ResponseEntity<>(new MessageResponse("El Usuario ya se encuentra en uso."), HttpStatus.CONFLICT);
            } else {
                Optional<Distrito> distrito_data =
                        distritoService.BuscarDistrito_By_IDDistrito(signupAgricultorRequest.getDistritoUsuario());

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

                    if (Code_SetUserRol.SetUserRol(agricultor, rol_data))
                        return new ResponseEntity<>(new MessageResponse("Ocurrió un error al otorgar sus permisos correspondientes."),
                                HttpStatus.NOT_FOUND);

                    //Asignando Fecha de Registro Actual
                    agricultor.setFecharegistroUsuario(LocalDate.now());

                    //Asignando Estado de Cuenta: PENDIENTE
                    agricultor.setEstadoUsuario("PENDIENTE");

                    //Asignando Imagen
                    try {
                        String separador_foto = Pattern.quote(".");
                        String[] formato_foto = Objects.requireNonNull(foto.getOriginalFilename()).split(separador_foto);
                        String nombre_foto = UUID.randomUUID() + "" + UUID.randomUUID()
                                + "." + formato_foto[formato_foto.length - 1];

                        String url_foto = ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/photos/")
                                .path(nombre_foto)
                                .toUriString();

                        if (!foto.isEmpty()) {
                            Imagen imagen = new Imagen(
                                    nombre_foto,
                                    foto.getContentType(),
                                    url_foto,
                                    foto.getBytes()
                            );

                            imagenService.GuardarImagen(imagen);
                            agricultor.setImagenUsuario(imagen);
                        } else {
                            InputStream fotoStream = getClass().getResourceAsStream("/static/img/AgroUser.png");
                            assert fotoStream != null;
                            byte[] file_foto = IOUtils.toByteArray(fotoStream);

                            Imagen imagen = new Imagen(
                                    nombre_foto + "png",
                                    "image/png",
                                    url_foto + "png",
                                    file_foto
                            );

                            imagenService.GuardarImagen(imagen);
                            agricultor.setImagenUsuario(imagen);
                        }

                        String token = RandomString.make(50);

                        UtilityToken utilityToken = new UtilityToken(
                                token,
                                "Signup Agricultor Verify",
                                LocalDateTime.now().plusHours(72)
                        );

                        Code_UtilityToken.UtilityTokenUser(agricultor, utilityToken, utilityTokenService, usuarioService);

                        String url = UtilityFenosys.GenerarUrl(request) + "/api/agricultor_verify_gateway?token=" + token;

                        EnviarCorreo(signupAgricultorRequest.getEmailUsuario(), url);
                    } catch (Exception e) {
                        return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e),
                                HttpStatus.EXPECTATION_FAILED);
                    }

                    usuarioService.GuardarUsuarioMultipart(agricultor, foto);

                    return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente. Revise su " +
                            "bandeja de entrada para verificar su cuenta, recuerde que dispone no más de 72 horas para " +
                            "culminar dicho proceso. De lo contrario, deberá rellenar el formulario nuevamente."),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar su Ubicación."),
                            HttpStatus.NOT_FOUND);
                }
            }
        }
    }

    @GetMapping("/agricultor_verify_gateway")
    void RedirectAgricultorVerify(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/agricultor/verify/" + token);
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PutMapping("/agricultor/signup/verify")
    public ResponseEntity<?> SignupAgricultorVerify(@RequestBody UtilityTokenRequest utilityTokenRequest) {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(utilityTokenRequest.getUtilityToken());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> agricultor_data = usuarioService.BuscarUsuario_By_UtilityToken(utilitytoken);

            if (agricultor_data.isPresent()) {
                Usuario agricultor = agricultor_data.get();

                agricultor.setEstadoUsuario("ACTIVO");
                usuarioService.GuardarUsuario(agricultor);

                utilityTokenService.EliminarUtilityToken_MiddleTable(utilitytoken.getIdUtilityToken());
                utilityTokenService.EliminarUtilityToken_This(utilitytoken.getIdUtilityToken());

                return new ResponseEntity<>(new MessageResponse("Se ha verificado el usuario satisfactoriamente."),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error durante la búsqueda del usuario."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error en el proceso de verificación."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private void EnviarCorreo(String email, String url) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("fenosys.assistant@gmail.com", "Fenosys Support");
        helper.setTo(email);

        String asunto = "Verificación de Cuenta";

        String contenido =
                "<h2>Hola,</h1>" +
                        "<p>Gracias por registrarte en Fenosys.</p>" +
                        "<br>Haz click en el link que se encuentra debajo para verificar su cuenta y tener acceso al sistema." +
                        "<a href=" + url + ">Verificar Mi Cuenta</a>";

        helper.setSubject(asunto);
        helper.setText(contenido, true);

        mailSender.send(message);
    }
}
