/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import org.springframework.web.multipart.MultipartFile;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.model.UtilityToken;

import java.util.Optional;

public interface IUsuarioService {

    //UTU: Utility Tokens de Usuario

    Optional<Usuario> BuscarUsuario_By_IDUsuario(Long id_usuario);

    Optional<Usuario> BuscarUsuario_By_EmailUsuario(String email_usuario);

    Optional<Usuario> BuscarUsuario_By_UsernameOrEmail(String username_or_email);

    Optional<Usuario> BuscarUsuario_By_UtilityToken(UtilityToken utilitytoken);

    Boolean ValidarUsername(String username_usuario);

    void GuardarUsuarioMultipart(Usuario usuario, MultipartFile archivo);

    void GuardarUsuario(Usuario usuario);

    void EliminarUsuario_From_UTU_MiddleTable(Long id_usuario);

    void EliminarUsuario_This(Long id_usuario);
}
