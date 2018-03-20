/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.server;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import model.client.Message;

/**
 * Classe Entitat JPA (persistència) que conté el missatge
 * @author Dani Machado
 */
@Entity
@Table(name="messages", schema="public")
public class MessageEntity extends Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    /**
     * Constructor per defecte
     */
    public MessageEntity() {
    }
}