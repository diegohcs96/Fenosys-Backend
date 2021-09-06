/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "restoretoken")
public class RestoreToken implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restoretoken")
    private Long idRestoreToken;

    @Column(name = "token_retoretoken")
    private String tokenRestoreToken;

    @Column(name = "razon_restoretoken")
    private String razonRestoreToken;

    @Column(name = "expiracion_restoretoken")
    private LocalDateTime expiracionRestoreToken;

    //Constructores
    public RestoreToken() {
    }

    public RestoreToken(String tokenRestoreToken, String razonRestoreToken, LocalDateTime expiracionRestoreToken) {
        this.tokenRestoreToken = tokenRestoreToken;
        this.razonRestoreToken = razonRestoreToken;
        this.expiracionRestoreToken = expiracionRestoreToken;
    }

    //Getters y Setters
    public Long getIdRestoreToken() {
        return idRestoreToken;
    }

    public void setIdRestoreToken(Long idRestoreToken) {
        this.idRestoreToken = idRestoreToken;
    }

    public String getTokenRestoreToken() {
        return tokenRestoreToken;
    }

    public void setTokenRestoreToken(String tokenRestoreToken) {
        this.tokenRestoreToken = tokenRestoreToken;
    }

    public String getRazonRestoreToken() {
        return razonRestoreToken;
    }

    public void setRazonRestoreToken(String razonRestoreToken) {
        this.razonRestoreToken = razonRestoreToken;
    }

    public LocalDateTime getExpiracionRestoreToken() {
        return expiracionRestoreToken;
    }

    public void setExpiracionRestoreToken(LocalDateTime expiracionRestoreToken) {
        this.expiracionRestoreToken = expiracionRestoreToken;
    }
}
