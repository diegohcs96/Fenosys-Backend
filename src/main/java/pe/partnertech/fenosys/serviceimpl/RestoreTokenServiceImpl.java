/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.repository.IRestoreTokenDAO;
import pe.partnertech.fenosys.service.IRestoreTokenService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RestoreTokenServiceImpl implements IRestoreTokenService {

    @Autowired
    IRestoreTokenDAO data;

    @Override
    public int GuardarRestoreToken(RestoreToken restoreToken) {
        int rpta = 0;

        RestoreToken rt = data.save(restoreToken);

        if (!rt.equals(null)) {
            rpta = 1;
        }

        return rpta;
    }

    @Override
    public Optional<RestoreToken> BuscarRestoreToken_Token(String token) {
        return data.findByTokenRestoreToken(token);
    }

    @Override
    public Set<RestoreToken> BuscarRestoreToken_ExpiracionyRazon(LocalDateTime expiracion, String razon) {
        return data.findRestoreTokenByExpiracionAndRazon(expiracion, razon);
    }

    @Override
    public void EliminarRestoreToken_MiddleTable(Long id) {
        data.deleteRestoreTokenFromMiddleTable(id);
    }

    @Override
    public void EliminarRestoreToken_This(Long id) {
        data.deleteById(id);
    }
}
