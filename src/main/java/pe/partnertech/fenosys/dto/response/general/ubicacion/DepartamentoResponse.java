/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.response.general.ubicacion;

public class DepartamentoResponse {

    //Atributos
    private String paisUbicacion;

    private String departamentoUbicacion;

    //Constructores
    public DepartamentoResponse() {
    }

    public DepartamentoResponse(String paisUbicacion, String departamentoUbicacion) {
        this.paisUbicacion = paisUbicacion;
        this.departamentoUbicacion = departamentoUbicacion;
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
}
