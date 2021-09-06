/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.service;

import pe.partnertech.fenosys.model.RestoreToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface IRestoreTokenService {

    int GuardarRestoreToken(RestoreToken restoreToken);

    Optional<RestoreToken> BuscarRestoreToken_Token(String token);

    Set<RestoreToken> BuscarRestoreToken_ExpiracionyRazon(LocalDateTime expiracion, String razon);

    void EliminarRestoreToken_MiddleTable(Long id);

    void EliminarRestoreToken_This(Long id);
}
