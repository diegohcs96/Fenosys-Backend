/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.request.usuario;

public class UpdateBasicInfoRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private Long iddistritoUsuario;

    //Constructores
    public UpdateBasicInfoRequest() {
    }

    public UpdateBasicInfoRequest(String nombreUsuario, String apellidoUsuario, Long iddistritoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.iddistritoUsuario = iddistritoUsuario;
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
}
