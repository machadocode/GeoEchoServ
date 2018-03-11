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
import model.client.RegisterApp;
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
        updateDefaultUsers();
    }
    
    private void init() {
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        users = userJpaControl.findUserEntities();
        closeEMF();     
    }
    
    private void createEMF(){
        emf =  Persistence.createEntityManagerFactory("geoechoservPU");      
    }
    private void closeEMF(){
        emf.close();        
    }
    
    private void updateDefaultUsers(){
        boolean updated = false;
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        try{
            if(!checkUser("user", "user1234", false)){
                User userNormal = new User("user", "user1234", "user@gmail.com", false);
                userJpaControl.create(userNormal);
                updated = true;
            }
            if(!checkUser("admin", "admin1234", true)){
                User userAdmin = new User("admin", "admin1234", "useradmin@gmail.com", true);
                userJpaControl.create(userAdmin);                
                updated = true;
            }
            if (updated) users = userJpaControl.findUserEntities();
        }catch(Exception ex){
                Logger.getLogger(ORMManager.class.getName()).log(Level.SEVERE, null, ex);            
        }finally{
            closeEMF();
        }
    }
    
    public boolean checkUser(String name, String password, boolean admin){
        for (User user : users){
            if(user.getUsername().equals(name) && user.getPassword().equals(password) && user.isAdminuser() == admin){
                return true;
            }            
        }
        return false;
    }
    
    public boolean checkUserAvailable(String name){
        for (User user : users){
            if(user.getUsername().equals(name)){
                return false;
            }            
        }
        return true;        
    }

    public boolean checkEmailAvailable(String email){
        for (User user : users){
            if(user.getEmail().equals(email)){
                return false;
            }            
        }
        return true;        
    }
    
    public boolean registerUser(RegisterApp register){
        if(checkUserAvailable(register.getUser())){
            if(checkEmailAvailable(register.getMail())){
                createEMF();
                UserJpaController userJpaControl = new UserJpaController(emf);
                User user = new User(register.getUser(), register.getPass(), register.getMail(), false);
                try{
                    userJpaControl.create(user);
                    users = userJpaControl.findUserEntities();
                }catch(Exception ex){
                    Logger.getLogger(ORMManager.class.getName()).log(Level.SEVERE, null, ex);                            
                    return false;
                }finally{
                    closeEMF();
                }
                return true;
            }
        }
        return false;
    }
    
}
