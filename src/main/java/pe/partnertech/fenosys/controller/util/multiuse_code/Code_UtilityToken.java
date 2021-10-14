/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.util.multiuse_code;

import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.model.UtilityToken;
import pe.partnertech.fenosys.service.IUsuarioService;
import pe.partnertech.fenosys.service.IUtilityTokenService;

import java.util.HashSet;
import java.util.Set;

public class Code_UtilityToken {

    public static void UtilityTokenUser(Usuario usuario, UtilityToken utilityToken,
                                        IUtilityTokenService utilityTokenService, IUsuarioService usuarioService) {
        utilityTokenService.GuardarUtilityToken(utilityToken);

        Set<UtilityToken> utilitytoken_list;
        if (usuario.getUtilitytokenUsuario() == null) {
            utilitytoken_list = new HashSet<>();
        } else {
            utilitytoken_list = usuario.getUtilitytokenUsuario();
        }
        utilitytoken_list.add(utilityToken);
        usuario.setUtilitytokenUsuario(utilitytoken_list);

        usuarioService.GuardarUsuario(usuario);
    }
}
