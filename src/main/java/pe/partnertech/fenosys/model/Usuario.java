/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", length = 50)
    private String nombreUsuario;

    @Column(name = "apellido_usuario", length = 50)
    private String apellidoUsuario;

    @Column(name = "email_usuario", length = 50)
    private String emailUsuario;

    @Column(name = "username_usuario", length = 30)
    private String usernameUsuario;

    @Column(name = "password_usuario")
    private String passwordUsuario;

    @Column(name = "fecharegistro_usuario")
    private LocalDate fecharegistroUsuario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_imagen",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_imagen", referencedColumnName = "id_imagen"))
    private Imagen imagenUsuario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_distrito",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_distrito", referencedColumnName = "id_distrito"))
    private Distrito distritoUsuario;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }, fetch = FetchType.LAZY)
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id_rol"))
    private Set<Rol> rolUsuario = new HashSet<>();

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "restoretokens_usuario",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_restoretoken", referencedColumnName = "id_restoretoken"))
    private Set<RestoreToken> restoretokenUsuario;

    @OneToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "planillascosecha_usuario",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_planillacosecha", referencedColumnName = "id_planillacosecha"))
    private Set<PlanillaCosecha> planillacosechaUsuario;

    //Constructores
    public Usuario() {
    }

    public Usuario(String nombreUsuario, String apellidoUsuario, String emailUsuario, String usernameUsuario,
                   String passwordUsuario, Distrito distritoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
        this.distritoUsuario = distritoUsuario;
    }

    //Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getUsernameUsuario() {
        return usernameUsuario;
    }

    public void setUsernameUsuario(String usernameUsuario) {
        this.usernameUsuario = usernameUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }

    public LocalDate getFecharegistroUsuario() {
        return fecharegistroUsuario;
    }

    public void setFecharegistroUsuario(LocalDate fecharegistroUsuario) {
        this.fecharegistroUsuario = fecharegistroUsuario;
    }

    public Imagen getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(Imagen imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public Distrito getDistritoUsuario() {
        return distritoUsuario;
    }

    public void setDistritoUsuario(Distrito distritoUsuario) {
        this.distritoUsuario = distritoUsuario;
    }

    public Set<Rol> getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(Set<Rol> rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public Set<RestoreToken> getRestoretokenUsuario() {
        return restoretokenUsuario;
    }

    public void setRestoretokenUsuario(Set<RestoreToken> restoretokenUsuario) {
        this.restoretokenUsuario = restoretokenUsuario;
    }

    public Set<PlanillaCosecha> getPlanillacosechaUsuario() {
        return planillacosechaUsuario;
    }

    public void setPlanillacosechaUsuario(Set<PlanillaCosecha> planillacosechaUsuario) {
        this.planillacosechaUsuario = planillacosechaUsuario;
    }
}
