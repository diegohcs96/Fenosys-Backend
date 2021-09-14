/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.usuario.signup;

public class SignupAgricultorRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private Long iddistritoUsuario;
    private String emailUsuario;
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupAgricultorRequest() {
    }

    public SignupAgricultorRequest(String nombreUsuario, String apellidoUsuario, Long iddistritoUsuario,
                                   String emailUsuario, String usernameUsuario, String passwordUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.iddistritoUsuario = iddistritoUsuario;
        this.emailUsuario = emailUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
    }

    //Getters y Setters
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public Long getIddistritoUsuario() {
        return iddistritoUsuario;
    }

    public void setIddistritoUsuario(Long iddistritoUsuario) {
        this.iddistritoUsuario = iddistritoUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getUsernameUsuario() {
        return usernameUsuario;
    }

    public void setUsernameUsuario(String usernameUsuario) {
        this.usernameUsuario = usernameUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }
}
