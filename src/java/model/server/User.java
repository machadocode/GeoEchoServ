/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package model.server;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Classe Entitat JPA per a la persistència dels usuaris del model de dades del servidor
 * @author Dani Machado
 */
@Entity
@Table(name="users", schema="public")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length=40, unique=true)
    private String username;
    @Column(length=20)
    private String password;
    @Column(length=40)
    private String email;
    private boolean adminuser;

    /**
     * Constructor per defecte
     */
    public User() {
    }
    
    /**
     * Constructor principal amb paràmetres assignats als atributs
     * @param username nom de l'usuari
     * @param password password de l'usuari
     * @param email email de l'usuari
     * @param adminuser  assigna rol d'administrador
     */
    public User(String username, String password, String email, boolean adminuser) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.adminuser = adminuser;
    }
    
    /**
     * Getter de l'id
     * @return retorna l'id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Setter de l'id
     * @param id assigna l'id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Getter del nom d'usuari
     * @return retorna el nom
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Setter del nom d'usuari
     * @param username assigna el nom
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Getter del password d'usuari
     * @return retorna el password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Setter del password d'usuari
     * @param password assigna el password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Getter de l'email d'usuari
     * @return retorna l'email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Setter del email d'usuari
     * @param email assigna l'email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter del rol administrador
     * @return retorna el rol administrador
     */
    public boolean isAdminuser() {
        return adminuser;
    }
    
    /**
     * Setter del rol administrador
     * @param adminuser assigna el rol administrador
     */
    public void setAdminuser(boolean adminuser) {
        this.adminuser = adminuser;
    }
    
    /**
     * Genera el hash
     * @return retorna el hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    /**
     * Mètode que compara la igualtat d'aquest objecte
     * @param object objecte a comparar
     * @return resultat comparació
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
     * Converteix a String
     * @return el String de l'objecte
     */
    @Override
    public String toString() {
        return "model.server.User[ id=" + id + " ]";
    }

}
