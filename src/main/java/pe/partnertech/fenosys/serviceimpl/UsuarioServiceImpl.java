/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.repository.IUsuarioDAO;
import pe.partnertech.fenosys.service.IUsuarioService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    IUsuarioDAO data;

    @Override
    public Optional<Usuario> BuscarUsuario_ID(Long id) {
        return data.findById(id);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_Username(String username) {
        return data.findByUsernameUsuario(username);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_Email(String email) {
        return data.findByEmailUsuario(email);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_Signin(String username_email) {
        return data.findByUsernameOrEmail(username_email);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_RestoreToken(RestoreToken restoreToken) {
        return data.findByRestoretokenUsuario(restoreToken);
    }

    @Override
    public Boolean ValidarUsername(String username) {
        return data.existsByUsernameUsuario(username);
    }

    @Override
    public Boolean ValidarEmail(String email) {
        return data.existsByEmailUsuario(email);
    }

    @Override
    public int GuardarUsuarioFull(Usuario usuario, MultipartFile archivo) {
        int rpta = 0;

        Usuario u = data.save(usuario);

        if (!u.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public int GuardarUsuarioSemiFull(Usuario usuario) {
        int rpta = 0;

        Usuario u = data.save(usuario);

        if (!u.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public void EliminarUsuario_From_RTU_MiddleTable(Long id) {
        data.deleteUsuarioFromRTUMiddleTable(id);
    }

    @Override
    public void EliminarUsuario_This(Long id) {
        data.deleteById(id);
    }
}
