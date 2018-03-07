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
    public Session createSession(ORMManager orm, Login login){
        Session session;
        if(checkLogin(orm, login)){
            session = new Session(true, createSessionId(login), login.getUser());
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
            if (session.getSessionId() == packet.getSessionId()) return true;
        }
        return false;
    }
    
    /**
     * Mètode que realitza el logout i mata la sessió
     * @param logout
     */
    public void logout(Logout logout){
        for(Session session : sessions){
            if (session.getSessionId() == logout.getSessionId()) sessions.remove(session);
        }        
    }
    
    /**
     * Mètode que comprova l'usuari i contrasenya del login
     * @param orm
     * @param login
     * @return 
     */
    public boolean checkLogin(ORMManager orm, Login login){        
        return orm.consultaUsuario(login.getUser(), login.getPass());
    }
    
    /**
     * Mètode que crea el Id de sessió
     * @param login
     * @return 
     */
    private int createSessionId(Login login){
        Calendar calendar = new GregorianCalendar();
        String cadena = login.getUser() + " - " + login.getPass() + " - " + calendar.getTime().toString();
        return cadena.hashCode();
    }

}
