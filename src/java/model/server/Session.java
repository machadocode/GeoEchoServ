/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package model.server;

import model.client.Packet;
import java.io.Serializable;
import java.util.Date;

/**
 * Classe que emmmagatzema la sessió de l'usuari del model de dades del servidor
 * @author Dani Machado
 */
public class Session extends Packet implements Serializable {

    private boolean alive;
    private String user;
    private Date Lastdate;

    /**
     * Constructor principal
     * @param alive True si està viva la sessió
     * @param sessionID Id de la sessió
     * @param user Nom de l'usuari
     */
    public Session(boolean alive, int sessionID, String user) {
        this.alive = alive;
        this.user = user;
        init(sessionID);
    }
    
    // Mètode privat per poder accedir a l'atribut privat sessionID de la classe abstracta Packet
    private void init(int sessionID){
        this.setSessionID(sessionID);
    }    
    
    /**
     * Getter d'alive
     * @return Retorna si la sessió està viva
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Setter d'alive
     * @param alive True si està viva
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Getter del nom d'usuari
     * @return Retorna el nom d'usuari
     */
    public String getUser() {
        return user;
    }

    /**
     * Setter del nom d'usuari
     * @param user Nom d'usuari
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Getter del LastDate
     * @return Retorna el LastDate
     */
    public Date getLastdate() {
        return Lastdate;
    }

    /**
     * Setter del LastDate
     * @param Lastdate El LastDate
     */
    public void setLastdate(Date Lastdate) {
        this.Lastdate = Lastdate;
    }
    
}