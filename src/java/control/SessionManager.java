/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.ArrayList;
import model.client.Login;
import model.client.Logout;
import model.server.Session;

/**
 * Classe estàtica que gestiona las sessions
 * @author Dani Machado
 */
public final class SessionManager {
    
    // Array estático que guarda las sesiones activas
    public static ArrayList<Session> sessions = new ArrayList<Session>();
    
    /**
     * Mètode que crea una sessió al fer login
     * @param login
     * @return 
     */
    public static Session createSession(Login login){
        if(checkLogin(login)){
            return new Session(true, createSessionId(login), login.getUser());
        }        
        return new Session(false, null, null);
    }
    
    /**
     * Mètode que comprova si la sessió està vigent
     * @param session
     * @return 
     */
    public static boolean checkSession(Session session){
        
        
        return false;
    }
    
    /**
     * Mètode que realitza el logout i mata la sessió
     * @param logout
     */
    public static void logout(Logout logout){
        
    }
    
    /**
     * Mètode que comprova l'usuari i contrasenya del login
     * @param login
     * @return 
     */
    public static boolean checkLogin(Login login){
        
        return false;
    }
    
    /**
     * Mètode que crea el Id de sessió
     * @param login
     * @return 
     */
    private static String createSessionId(Login login){
        
        
        return null;
    }

}
