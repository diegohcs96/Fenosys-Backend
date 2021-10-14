/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.tools;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.model.UtilityToken;
import pe.partnertech.fenosys.service.IUsuarioService;
import pe.partnertech.fenosys.service.IUtilityTokenService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class MaintainceFenosys {

    final
    IUtilityTokenService utilityTokenService;

    final
    IUsuarioService usuarioService;

    public MaintainceFenosys(IUtilityTokenService utilityTokenService, IUsuarioService usuarioService) {
        this.utilityTokenService = utilityTokenService;
        this.usuarioService = usuarioService;
    }

    private void UtilityTokenList(Set<UtilityToken> utilitytoken_list) {
        for (UtilityToken utilitytoken : utilitytoken_list) {
            Optional<Usuario> usuario_timedout = usuarioService.BuscarUsuario_By_UtilityToken(utilitytoken);

            if (usuario_timedout.isPresent()) {
                Usuario usuario = usuario_timedout.get();

                utilityTokenService.EliminarUtilityToken_MiddleTable(utilitytoken.getIdUtilityToken());
                utilityTokenService.EliminarUtilityToken_This(utilitytoken.getIdUtilityToken());

                usuarioService.EliminarUsuario_From_UTU_MiddleTable(usuario.getIdUsuario());
                usuarioService.EliminarUsuario_This(usuario.getIdUsuario());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarUsuariosTimedOutSignupAdmin() {
        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(LocalDateTime.now(),
                "Signup Admin");

        UtilityTokenList(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarUsuariosTimedOutSignupAgricultor() {
        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(LocalDateTime.now(),
                "Signup Agricultor Verify");

        UtilityTokenList(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarRestoreToken() {
        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(LocalDateTime.now(),
                "Restore Password");

        for (UtilityToken restoretoken : utilitytoken_list) {
            long id = restoretoken.getIdUtilityToken();

            utilityTokenService.EliminarUtilityToken_MiddleTable(id);
            utilityTokenService.EliminarUtilityToken_This(id);
        }
    }
}


