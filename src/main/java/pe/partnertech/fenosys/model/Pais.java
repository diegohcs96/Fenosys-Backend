/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "pais")
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Long idPais;

    @Column(name = "nombre_pais")
    private String nombrePais;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "departamentos_pais",
            joinColumns = @JoinColumn(name = "id_pais", referencedColumnName = "id_pais"),
            inverseJoinColumns = @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento"))
    private Set<Departamento> departamentosPais;

    //Constructores
    public Pais() {
    }

    public Pais(String nombrePais, Set<Departamento> departamentosPais) {
        this.nombrePais = nombrePais;
        this.departamentosPais = departamentosPais;
    }

    //Getters y Setters
    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }
}
