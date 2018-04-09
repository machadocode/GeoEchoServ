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
import model.client.User;

/**
 * Classe Entitat JPA (persistència) dels usuaris del model de dades del servidor
 * @author Dani Machado
 */
@Entity
@Table(name="users", schema="public")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length=40, unique=true)
    protected String username;
    @Column(length=32)
    protected String password;
    @Column(length=40)
    protected String email;
    protected boolean adminuser;
    protected boolean banned;

    /**
     * Constructor per defecte
     */
    public UserEntity() {
    }
    
    /**
     * Constructor principal
     * @param username
     * @param password
     * @param email
     * @param adminuser
     * @param banned 
     */
    public UserEntity(String username, String password, String email, boolean adminuser, boolean banned) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.adminuser = adminuser;
        this.banned = banned;
    }
    
    /**
     * Constructor que mapa l'usuari del model user client amb el model user entitat
     * @param user 
     */
    public UserEntity(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.adminuser = user.isAdminuser();
        this.banned = user.isBanned();
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
     * Getter username
     * @return 
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Setter username
     * @param username 
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Getter password
     * @return 
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Setter password
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Getter email
     * @return 
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Setter email
     * @param email 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Getter adminuser
     * @return 
     */
    public boolean isAdminuser() {
        return adminuser;
    }
    
    /**
     * Setter adminuser
     * @param adminuser 
     */
    public void setAdminuser(boolean adminuser) {
        this.adminuser = adminuser;
    }
    
    /**
     * Getter banned
     * @return 
     */
    public boolean isBanned() {
        return banned;
    }
    
    /**
     * Setter banned
     * @param banned 
     */
    public void setBanned(boolean banned) {
        this.banned = banned;
    }
    
}
