/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package control.session;

import control.persistence.ORMManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import model.client.Login;
import model.client.Logout;
import model.client.Packet;
import model.server.Session;

/**
 * Classe que gestiona las sessions d'usuari del sistema
 * @author Dani Machado
 */
public final class SessionManager {
    
    /**
     * Temps de vida de les sessions (1h = 3600000 miliseconds)
     */ 
    public static final long ALIVE_TIME = 3600000;
    
    /**
     * Array que guarda las sessions actives
     */
    public ArrayList<Session> sessions = new ArrayList<>();
    
    /**
     * Mètode que crea una sessió al fer login
     * @param orm Gestor de persistència ORM
     * @param login Objecte login amb les dades
     * @param admin Rol de l'usuari
     * @return Retorna un objecte de sessió
     */
    public Session createSession(ORMManager orm, Login login, boolean admin){
        Session session;
        if(checkLogin(orm, login, admin)){
            killOlderSessions();        // Mata les session més antigues del temps estipulat a ALIVE_TIME
            session = new Session(true, createSessionId(login.getUser(), login.getPass()), login.getUser(), admin);
            sessions.add(session);
            return session;
        }        
        return new Session(false, 0, null, false);
    }
    
    /**
     * Mètode que comprova si la sessió està vigent
     * @param packet Objecte Packet rebut pel servidor
     * @return Retorna la comrpovació de si està activa la sessió
     */
    public boolean checkSession(Packet packet){
        killOlderSessions();        // Mata les session més antigues del temps estipulat a ALIVE_TIME
        for(Session session : sessions){
            if (session.getSessionID() == packet.getSessionID()) {
                session.setLastdate(new GregorianCalendar().getTime());
                return true;
            }
        }
        return false;
    }
    
    /**
     * Mètode que realitza el logout i mata la sessió
     * @param logout Objecte Logout rebut pel servidor
     * @return Retorna true si ha tingut èxit
     */
    public boolean logout(Logout logout){
        Session sessionToKill = null;
        for(Session session : sessions){
            if (session.getSessionID()== logout.getSessionID()) sessionToKill = session;
        }
        if(sessionToKill != null) {
            sessions.remove(sessionToKill);
            return true;
        }
        return false;
    }
    
    /**
     * Mètode que comprova l'usuari i contrasenya del login
     * @param orm Gestor de persistència ORM
     * @param login Objecte Login
     * @param admin Rol del l'usuari
     * @return Retorna true si el login és possible
     */
    public boolean checkLogin(ORMManager orm, Login login, boolean admin){        
        return orm.checkUser(login.getUser(), login.getPass(), admin);
    }
    
    /**
     * Mètode que crea el Id de sessió a partir del login
     * @param user nom de l'usuari
     * @param password password de l'usuari
     * @return Retorna true si s'ha pogut crear la sessió
     */
    public int createSessionId(String user, String password){
        Calendar calendar = new GregorianCalendar();
        String cadena = user + " - " + password + " - " + calendar.getTime().toString();
        return cadena.hashCode();
    }
    
    /**
     * Mètode que comprova el temps transcurregut de les sessions i elimina les que passen del temps definit a la constant ALIVE_TIME
     */
    public void killOlderSessions(){
        Date today = new GregorianCalendar().getTime();
        ArrayList<Session> arraySessionList = new ArrayList<>();        
        for(Session session : sessions){            
            if(today.getTime() - session.getLastdate().getTime() < ALIVE_TIME){
                arraySessionList.add(session);
            }
        }
        sessions = arraySessionList;
    }
    
    /**
     * Mètode que comprova si una sessió pertany a un usuari admin
     * @param packet
     * @return 
     */    
    public boolean checkSessionAdmin(Packet packet){
        for(Session session : sessions){
            if (session.getSessionID() == packet.getSessionID() && session.isAdmin()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retorna l'objecte sessió d'un paquet si n'hi vigent
     * @param packet
     * @return 
     */
    public Session getPacketSession(Packet packet){
        for(Session session : sessions){
            if (session.getSessionID() == packet.getSessionID()) {
                return session;
            }
        }
        return null;
    }

}
