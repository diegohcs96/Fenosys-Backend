/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.response.general.ubicacion;

public class ProvinciaResponse {

    //Atributos
    private String paisUbicacion;

    private String departamentoUbicacion;

    private String provinciaUbicacion;

    //Constructores
    public ProvinciaResponse() {
    }

    public ProvinciaResponse(String paisUbicacion, String departamentoUbicacion, String provinciaUbicacion) {
        this.paisUbicacion = paisUbicacion;
        this.departamentoUbicacion = departamentoUbicacion;
        this.provinciaUbicacion = provinciaUbicacion;
    }

    //Getters y Setters
    public String getPaisUbicacion() {
        return paisUbicacion;
    }

    public void setPaisUbicacion(String paisUbicacion) {
        this.paisUbicacion = paisUbicacion;
    }

    public String getDepartamentoUbicacion() {
        return departamentoUbicacion;
    }

    public void setDepartamentoUbicacion(String departamentoUbicacion) {
        this.departamentoUbicacion = departamentoUbicacion;
    }

    public String getProvinciaUbicacion() {
        return provinciaUbicacion;
    }

    public void setProvinciaUbicacion(String provinciaUbicacion) {
        this.provinciaUbicacion = provinciaUbicacion;
    }
}
