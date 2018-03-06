/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.server;

import model.client.Packet;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Dani Machado
 */
public class Session extends Packet implements Serializable {

    private boolean alive;
    private String sessionID;
    private String user;
    private Date Lastdate;

    public Session(boolean alive, String sessionID, String user) {
        this.alive = alive;
        this.sessionID = sessionID;
        this.user = user;
    }
    
    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getLastdate() {
        return Lastdate;
    }

    public void setLastdate(Date Lastdate) {
        this.Lastdate = Lastdate;
    }
    
}