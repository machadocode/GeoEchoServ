/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control.persistence;

import control.persistence.jpa.UserJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.server.User;

/**
 * Classe que gestiona la persistència de les dades
 * @author Dani Machado
 */
public final class ORMManager {
    
    private EntityManagerFactory emf;
    private List<User> users;

    public ORMManager() {
        init();
    }
    
    private void init() {
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        users = userJpaControl.findUsersEntities();
        try{
            if(!consultaUsuario("user", "user1234")){
                User userNormal = new User("user", "user1234", "user@gmail.com", false);
                userJpaControl.create(userNormal);                
            }
            if(!consultaUsuario("admin", "admin1234")){
                User userAdmin = new User("admin", "admin1234", "useradmin@gmail.com", true);
                userJpaControl.create(userAdmin);                
            } 
        }catch(Exception ex){
                Logger.getLogger(ORMManager.class.getName()).log(Level.SEVERE, null, ex);            
        }finally{
            closeEMF();
        }
    }
    
    private void createEMF(){
        emf =  Persistence.createEntityManagerFactory("geoechoservPU");      
    }
    private void closeEMF(){
        emf.close();        
    }
    
    public boolean consultaUsuario(String name, String password){
        for (User user : users){
            if(user.getUsername().equals(name) && user.getPassword().equals(password)){
                return true;
            }            
        }
        return false;
    }  
    
}
