/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.service.IRestoreTokenService;
import pe.partnertech.fenosys.service.IUsuarioService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class MaintainceFenosys {

    @Autowired
    IRestoreTokenService restoreTokenService;

    @Autowired
    IUsuarioService usuarioService;

    @Scheduled(fixedRate = 60000)
    public void EliminarUsuariosTimedOutSignupAdmin() {

        Set<RestoreToken> restoretoken_list = restoreTokenService.BuscarRestoreToken_ExpiracionyRazon(LocalDateTime.now(), "Signup Admin");

        for (RestoreToken restoretoken : restoretoken_list) {
            Optional<Usuario> usuario_timedout = usuarioService.BuscarUsuario_RestoreToken(restoretoken);

            if (usuario_timedout.isPresent()) {
                Usuario usuario = usuario_timedout.get();

                restoreTokenService.EliminarRestoreToken_MiddleTable(restoretoken.getIdRestoreToken());
                restoreTokenService.EliminarRestoreToken_This(restoretoken.getIdRestoreToken());

                usuarioService.EliminarUsuario_From_RTU_MiddleTable(usuario.getIdUsuario());
                usuarioService.EliminarUsuario_This(usuario.getIdUsuario());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarRestoreToken() {

        Set<RestoreToken> restoretoken_list = restoreTokenService.BuscarRestoreToken_ExpiracionyRazon(LocalDateTime.now(), "Restore Password");
        for (RestoreToken restoretoken : restoretoken_list) {
            long id = restoretoken.getIdRestoreToken();
            restoreTokenService.EliminarRestoreToken_MiddleTable(id);
            restoreTokenService.EliminarRestoreToken_This(id);
        }
    }
}


