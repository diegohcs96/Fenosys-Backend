/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.restore_password;

public class RestorePasswordRequest {

    //Atributos
    private String emailUsuario;

    //Constructores
    public RestorePasswordRequest() {
    }

    public RestorePasswordRequest(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    //Getters y Setters
    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
