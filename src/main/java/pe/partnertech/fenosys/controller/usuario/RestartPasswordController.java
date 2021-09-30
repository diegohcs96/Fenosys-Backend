/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.request.restore_password.RestartPasswordRequest;
import pe.partnertech.fenosys.dto.request.restore_password.RestorePasswordRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IRestoreTokenService;
import pe.partnertech.fenosys.service.IUsuarioService;
import pe.partnertech.fenosys.tools.UtilityFenologia;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RestartPasswordController {

    @Autowired
    IUsuarioService usuarioService;
    @Autowired
    IRestoreTokenService restoreTokenService;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${front.baseurl}")
    private String baseurl;

    @PostMapping("/restart_password/send")
    public ResponseEntity<?> RestartPasswordSendEmail(@RequestBody RestorePasswordRequest restorePasswordRequest,
                                                      HttpServletRequest request) {

        String token = RandomString.make(50);

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_Email(restorePasswordRequest.getEmailUsuario());

        if (usuario_data.isPresent()) {
            try {
                Usuario usuario = usuario_data.get();

                RestoreToken restoreToken = new RestoreToken(
                        token,
                        "Restore Password",
                        LocalDateTime.now().plusMinutes(10)
                );

                restoreTokenService.GuardarRestoreToken(restoreToken);

                Set<RestoreToken> restoretoken_list;
                if (usuario.getRestoretokenUsuario() == null) {
                    restoretoken_list = new HashSet<>();
                } else {
                    restoretoken_list = usuario.getRestoretokenUsuario();
                }
                restoretoken_list.add(restoreToken);
                usuario.setRestoretokenUsuario(restoretoken_list);

                usuarioService.GuardarUsuarioSemiFull(usuario);

                String url = UtilityFenologia.GenerarUrl(request) + "/api/password_restart_gateway?token=" + token;

                EnviarCorreo(restorePasswordRequest.getEmailUsuario(), url);
            } catch (UnsupportedEncodingException e) {
                return new ResponseEntity<>(new MessageResponse("Error: " + e), HttpStatus.BAD_REQUEST);
            } catch (MessagingException e) {
                return new ResponseEntity<>(new MessageResponse("Error al enviar el email."), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new MessageResponse("Revise su bandeja de entrada para continuar con el proceso " +
                    "de Reinicio de Contraseña."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encuentra el email solicitado en nuestro sistema."), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/password_restart_gateway")
    void RedirectSignupAdminRequest(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<RestoreToken> restoretoken_data = restoreTokenService.BuscarRestoreToken_Token(token);

        if (restoretoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/password/restart/" + token);
        } else {
            response.sendRedirect(baseurl);
        }
    }

    @PutMapping("/restart_password/update")
    public ResponseEntity<?> ForgotPasswordUpdateProcess(@RequestBody RestartPasswordRequest restartPasswordRequest) {

        Optional<RestoreToken> restoretoken_data = restoreTokenService.BuscarRestoreToken_Token(restartPasswordRequest.getRestoreToken());

        if (restoretoken_data.isPresent()) {
            RestoreToken restoretoken = restoretoken_data.get();

            Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_RestoreToken(restoretoken);

            if (usuario_data.isPresent()) {
                Usuario usuario = usuario_data.get();

                usuario.setPasswordUsuario(passwordEncoder.encode(restartPasswordRequest.getPasswordUsuario()));

                usuarioService.GuardarUsuarioSemiFull(usuario);

                return new ResponseEntity<>(new MessageResponse("Contraseña actualizada satisfactoriamente"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al procesar su solicitud."), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al procesar su solicitud."), HttpStatus.NOT_FOUND);
        }
    }

    private void EnviarCorreo(String email, String url) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("fenosis.assistant@gmail.com", "Fenosis Support");
        helper.setTo(email);

        String asunto = "Solicitud de Reinicio de Contraseña";

        String contenido =
                "<h2>Hola,</h1>" +
                        "<p>Gracias por realizar tu solicitud de Reinicio de Contraseña.</p>" +
                        "<br>Haz click en el link que se encuentra debajo para continuar con el proceso." +
                        "<a href=" + url + ">Reiniciar mi Contraseña</a>";

        helper.setSubject(asunto);
        helper.setText(contenido, true);

        mailSender.send(message);
    }
}
