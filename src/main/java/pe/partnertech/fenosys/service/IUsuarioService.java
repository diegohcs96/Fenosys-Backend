/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import org.springframework.web.multipart.MultipartFile;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.model.Usuario;

import java.util.Optional;

public interface IUsuarioService {

    Optional<Usuario> BuscarUsuario_ID(Long id);

    Optional<Usuario> BuscarUsuario_Username(String username);

    Optional<Usuario> BuscarUsuario_Email(String email);

    Optional<Usuario> BuscarUsuario_Signin(String username_email);

    Optional<Usuario> BuscarUsuario_RestoreToken(RestoreToken restoreToken);

    Boolean ValidarUsername(String username);

    Boolean ValidarEmail(String email);

    int GuardarUsuarioFull(Usuario usuario, MultipartFile archivo);

    int GuardarUsuarioSemiFull(Usuario usuario);

    void EliminarUsuario_From_RTU_MiddleTable(Long id);

    void EliminarUsuario_This(Long id);
}
