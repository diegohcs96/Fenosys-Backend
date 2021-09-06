/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ubicacion")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private int idUbicacion;

    @Column(name = "pais_ubicacion", length = 50)
    private String paisUbicacion;

    //Departamento = Estado
    @Column(name = "departamento_ubicacion", length = 50)
    private String departamentoUbicacion;

    //Provincia = Ciudad
    @Column(name = "provincia_ubicacion", length = 50)
    private String provinciaUbicacion;

    //Distrito = Area
    @Column(name = "distrito_ubicacion", length = 50)
    private String distritoUbicacion;

    //Constructores
    public Ubicacion() {
    }

    public Ubicacion(String paisUbicacion, String departamentoUbicacion, String provinciaUbicacion,
                     String distritoUbicacion) {
        this.paisUbicacion = paisUbicacion;
        this.departamentoUbicacion = departamentoUbicacion;
        this.provinciaUbicacion = provinciaUbicacion;
        this.distritoUbicacion = distritoUbicacion;
    }

    //Getters y Setters
    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

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
