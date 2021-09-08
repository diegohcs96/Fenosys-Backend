/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.response.general.ubicacion;

public class DistritoResponse {

    //Atributos
    private String paisUbicacion;

    private String departamentoUbicacion;

    private String provinciaUbicacion;

    private String distritoUbicacion;

    //Constructores
    public DistritoResponse() {
    }

    public DistritoResponse(String paisUbicacion, String departamentoUbicacion, String provinciaUbicacion,
                            String distritoUbicacion) {
        this.paisUbicacion = paisUbicacion;
        this.departamentoUbicacion = departamentoUbicacion;
        this.provinciaUbicacion = provinciaUbicacion;
        this.distritoUbicacion = distritoUbicacion;
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

    public String getDistritoUbicacion() {
        return distritoUbicacion;
    }

    public void setDistritoUbicacion(String distritoUbicacion) {
        this.distritoUbicacion = distritoUbicacion;
    }
}
