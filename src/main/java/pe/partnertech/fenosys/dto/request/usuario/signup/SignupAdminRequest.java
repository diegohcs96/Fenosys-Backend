/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.usuario.signup;

public class SignupAdminRequest {

    //Atributos
    private String restoretokenUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private Long iddistritoUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupAdminRequest() {
    }

    public SignupAdminRequest(String restoretokenUsuario, String nombreUsuario, String apellidoUsuario,
                              Long iddistritoUsuario, String passwordUsuario) {
        this.restoretokenUsuario = restoretokenUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.iddistritoUsuario = iddistritoUsuario;
        this.passwordUsuario = passwordUsuario;
    }

    //Getters y Setters
    public String getRestoretokenUsuario() {
        return restoretokenUsuario;
    }

    public void setRestoretokenUsuario(String restoretokenUsuario) {
        this.restoretokenUsuario = restoretokenUsuario;
    }

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

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }
}
