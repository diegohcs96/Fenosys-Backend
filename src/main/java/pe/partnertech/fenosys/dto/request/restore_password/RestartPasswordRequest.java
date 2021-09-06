/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.restore_password;

public class RestartPasswordRequest {

    //Atributos
    private String restoreToken;
    private String passwordUsuario;

    //Constructores

    public RestartPasswordRequest() {
    }

    public RestartPasswordRequest(String restoreToken, String passwordUsuario) {
        this.restoreToken = restoreToken;
        this.passwordUsuario = passwordUsuario;
    }

    //Getters y Setters
    public String getRestoreToken() {
        return restoreToken;
    }

    public void setRestoreToken(String restoreToken) {
        this.restoreToken = restoreToken;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }
}
