/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.usuario.signup;

public class SignupAgricultorRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private String paisUsuario;
    private String departamentoUsuario;
    private String provinciaUsuario;
    private String distritoUsuario;
    private String emailUsuario;
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupAgricultorRequest() {
    }

    public SignupAgricultorRequest(String nombreUsuario, String apellidoUsuario, String paisUsuario,
                                   String departamentoUsuario, String provinciaUsuario, String distritoUsuario,
                                   String emailUsuario, String usernameUsuario, String passwordUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.paisUsuario = paisUsuario;
        this.departamentoUsuario = departamentoUsuario;
        this.provinciaUsuario = provinciaUsuario;
        this.distritoUsuario = distritoUsuario;
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

    public String getPaisUsuario() {
        return paisUsuario;
    }

    public void setPaisUsuario(String paisUsuario) {
        this.paisUsuario = paisUsuario;
    }

    public String getDepartamentoUsuario() {
        return departamentoUsuario;
    }

    public void setDepartamentoUsuario(String departamentoUsuario) {
        this.departamentoUsuario = departamentoUsuario;
    }

    public String getProvinciaUsuario() {
        return provinciaUsuario;
    }

    public void setProvinciaUsuario(String provinciaUsuario) {
        this.provinciaUsuario = provinciaUsuario;
    }

    public String getDistritoUsuario() {
        return distritoUsuario;
    }

    public void setDistritoUsuario(String distritoUsuario) {
        this.distritoUsuario = distritoUsuario;
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
