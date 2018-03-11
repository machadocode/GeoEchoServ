/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control.session;

import control.persistence.ORMManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import model.client.Login;
import model.client.Logout;
import model.client.Packet;
import model.server.Session;

/**
 * Classe estàtica que gestiona las sessions
 * @author Dani Machado
 */
public final class SessionManager {
    
    // Array estático que guarda las sesiones activas
    public ArrayList<Session> sessions = new ArrayList<>();
    
    /**
     * Mètode que crea una sessió al fer login
     * @param orm
     * @param login
     * @return 
     */
    public Session createSession(ORMManager orm, Login login, boolean admin){
        Session session;
        if(checkLogin(orm, login, admin)){
            session = new Session(true, createSessionId(login.getUser(), login.getPass()), login.getUser());
            sessions.add(session);
            return session;
        }        
        return new Session(false, 0, null);
    }
    
    /**
     * Mètode que comprova si la sessió està vigent
     * @param packet
     * @return 
     */
    public boolean checkSession(Packet packet){
        for(Session session : sessions){
            if (session.getSessionID() == packet.getSessionID()) return true;
        }
        return false;
    }
    
    /**
     * Mètode que realitza el logout i mata la sessió
     * @param logout
     */
    public void logout(Logout logout){
        Session sessionToKill = null;
        for(Session session : sessions){
            if (session.getSessionID()== logout.getSessionID()) sessionToKill = session;
        }
        if(sessionToKill != null) sessions.remove(sessionToKill);
    }
    
    /**
     * Mètode que comprova l'usuari i contrasenya del login
     * @param orm
     * @param login
     * @return 
     */
    public boolean checkLogin(ORMManager orm, Login login, boolean admin){        
        return orm.checkUser(login.getUser(), login.getPass(), admin);
        //return true;
    }
    
    /**
     * Mètode que crea el Id de sessió a partir del login
     * @param user
     * @param password
     * @return 
     */
    public int createSessionId(String user, String password){
        Calendar calendar = new GregorianCalendar();
        String cadena = user + " - " + password + " - " + calendar.getTime().toString();
        return cadena.hashCode();
    }

}
