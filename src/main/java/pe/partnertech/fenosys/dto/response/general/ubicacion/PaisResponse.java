/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.dto.response.general.ubicacion;

public class PaisResponse {

    //Atributos
    private String paisUbicacion;

    //Constructores
    public PaisResponse() {
    }

    public PaisResponse(String paisUbicacion) {
        this.paisUbicacion = paisUbicacion;
    }

    //Getters y Setters
    public String getPaisUbicacion() {
        return paisUbicacion;
    }

    public void setPaisUbicacion(String paisUbicacion) {
        this.paisUbicacion = paisUbicacion;
    }
}
