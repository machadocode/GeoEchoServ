/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package model.server;

import java.io.Serializable;
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
public class UserEntity extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Constructor per defecte
     */
    public UserEntity() {
    }

    public UserEntity(String username, String password, String email, boolean adminuser, boolean banned) {
        super(username, password, email, adminuser, banned);
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

}
