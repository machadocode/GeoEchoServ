/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.ArrayList;
import model.Login;
import model.Session;

/**
 * 
 * @author Dani Machado
 */
public final class SessionManager {
    
    public static ArrayList<Session> sessions = new ArrayList<Session>();
        
    public static Session createSession(Login login){
        
        if(checkLogin(login)){
            return new Session(true, createSessionId(), login.getUser());
        }        
        return new Session(false, null, null);
    }
    
    public static boolean checkSession(Session session){
        
        
        return false;
    }
    
    public static void logout(){
        
    }
    
    public static boolean checkLogin(Login login){
        
        return false;
    }
    
    private static String createSessionId(){
        
        
        return null;
    }

}
