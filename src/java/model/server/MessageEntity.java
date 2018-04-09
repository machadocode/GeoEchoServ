/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package model.server;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import model.client.Message;

/**
 * Classe Entitat JPA (persistència) que conté el missatge
 * @author Dani Machado
 */
@Entity
@Table(name="messages", schema="public")
public class MessageEntity implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    protected float coordX;
    protected float coordY;
    protected String text;
    @Column
    @Lob
    protected String photoBase64;
    protected String userSender;
    protected String userReceiver;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date dateMsg;
    protected int life;
    protected boolean msgPublic;
    protected boolean msgVisible;
    protected boolean msgReaded;
    
    /**
     * Constructor per defecte
     */
    public MessageEntity() {
    }
    /**
     * Constructor principal
     * @param coordX
     * @param coordY
     * @param text
     * @param photoBase64
     * @param userSender
     * @param userReceiver
     * @param dateMsg
     * @param life
     * @param msgPublic
     * @param msgVisible
     * @param msgReaded 
     */
    public MessageEntity(float coordX, float coordY, String text, String photoBase64, String userSender, String userReceiver, Date dateMsg, int life, boolean msgPublic, boolean msgVisible, boolean msgReaded) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.text = text;
        this.photoBase64 = photoBase64;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.dateMsg = dateMsg;
        this.life = life;
        this.msgPublic = msgPublic;
        this.msgVisible = msgVisible;
        this.msgReaded = msgReaded;
    }
    
    /**
     * Constructor que mapea los atributos de un Message cliente a uno Entidad 
     * @param message 
     */
    public MessageEntity(Message message) {
        this.coordX = message.getCoordX();
        this.coordY = message.getCoordY();
        this.text = message.getText();
        this.photoBase64 = message.getPhotoBase64();
        this.userSender = message.getUserSender();
        this.userReceiver = message.getUserReceiver();
        this.dateMsg = message.getDate();
        this.life = message.getLife();
        this.msgPublic = message.isMsgPublic();
        this.msgVisible = message.isMsgVisible();
        this.msgReaded = message.isMsgReaded();
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
     * Getter coordX
     * @return 
     */
    public float getCoordX() {
        return coordX;
    }
    /**
     * Setter coordX
     * @param coordX 
     */
    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }
    /**
     * Getter coordY
     * @return 
     */
    public float getCoordY() {
        return coordY;
    }
    /**
     * Setter coordY
     * @param coordY 
     */
    public void setCoordY(float coordY) {
        this.coordY = coordY;
    }
    
    /**
     * Getter text
     * @return 
     */
    public String getText() {
        return text;
    }
    
    /**
     * Setter text
     * @param text 
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Getter photo
     * @return 
     */
    public String getPhotoBase64() {
        return photoBase64;
    }
    
    /**
     * Setter photo
     * @param photoBase64 
     */
    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    /**
     * Getter userSender
     * @return 
     */
    public String getUserSender() {
        return userSender;
    }
    
    /**
     * Setter userSender
     * @param userSender 
     */
    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    /**
     * Getter userReceiver
     * @return 
     */
    public String getUserReceiver() {
        return userReceiver;
    }
    
    /**
     * Setter userReceiver
     * @param userReceiver 
     */
    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }
    
    /**
     * Getter date
     * @return 
     */
    public Date getDateMsg() {
        return dateMsg;
    }
    
    /**
     * Setter date
     * @param dateMsg
     */
    public void setDateMsg(Date dateMsg) {
        this.dateMsg = dateMsg;
    }
    
    /**
     * Getter life
     * @return 
     */
    public int getLife() {
        return life;
    }
    
    /**
     * Setter life
     * @param life 
     */
    public void setLife(int life) {
        this.life = life;
    }
    
    /**
     * Getter msgPublic
     * @return 
     */
    public boolean isMsgPublic() {
        return msgPublic;
    }

    /**
     * Setter msgPublic
     * @param msgPublic 
     */
    public void setMsgPublic(boolean msgPublic) {
        this.msgPublic = msgPublic;
    }
    
    /**
     * Getter msgVivible
     * @return 
     */
    public boolean isMsgVisible() {
        return msgVisible;
    }
    
    /**
     * Setter msgVisible
     * @param msgVisible 
     */
    public void setMsgVisible(boolean msgVisible) {
        this.msgVisible = msgVisible;
    }
    
    /**
     * Getter msgReader
     * @return 
     */
    public boolean isMsgReaded() {
        return msgReaded;
    }
    
    /**
     * Setter msgReader
     * @param msgReaded 
     */
    public void setMsgReaded(boolean msgReaded) {
        this.msgReaded = msgReaded;
    }
}