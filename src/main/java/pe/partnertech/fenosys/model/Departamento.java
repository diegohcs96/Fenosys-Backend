/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "departamento")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Long idDepartamento;

    @Column(name = "nombre_departamento")
    private String nombreDepartamento;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "provincias_departamento",
            joinColumns = @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento"),
            inverseJoinColumns = @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia"))
    private Set<Provincia> provinciasDepartamento;

    //Constructores
    public Departamento() {
    }

    public Departamento(String nombreDepartamento, Set<Provincia> provinciasDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
        this.provinciasDepartamento = provinciasDepartamento;
    }

    //Getters y Setters
    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Set<Provincia> getProvinciasDepartamento() {
        return provinciasDepartamento;
    }

    public void setProvinciasDepartamento(Set<Provincia> provinciasDepartamento) {
        this.provinciasDepartamento = provinciasDepartamento;
    }
}
