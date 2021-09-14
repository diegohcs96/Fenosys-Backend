/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "distrito")
public class Distrito implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrito")
    private Long idDistrito;

    @Column(name = "nombre_distrito")
    private String nombreDistrito;

    //Constructores
    public Distrito() {
    }

    public Distrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }

    //Getters y Setters
    public Long getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(Long idDistrito) {
        this.idDistrito = idDistrito;
    }

    public String getNombreDistrito() {
        return nombreDistrito;
    }

    public void setNombreDistrito(String nombreDistrito) {
        this.nombreDistrito = nombreDistrito;
    }
}
