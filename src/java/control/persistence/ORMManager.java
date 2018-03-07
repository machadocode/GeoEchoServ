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
        initialValues();
    }
    
    private void init() {
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        users = userJpaControl.findUsersEntities();
        closeEMF();
    }
    
    private void initialValues(){
        createInitialUser();
    }
    
    private void createEMF(){
        emf =  Persistence.createEntityManagerFactory("geoechoservPU");      
    }
    private void closeEMF(){
        emf.close();        
    }
    
    /**
     * Crea els usuaris per defecte
     */
    private void createInitialUser(){
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        User userNormal = new User("user", "user1234", "user@gmail.com", false);
        User userAdmin = new User("admin", "admin1234", "useradmin@gmail.com", true);
        try{
            if(!consultaUsuario("user", "user1234")){
                userJpaControl.create(userNormal);
            }            
            if(!consultaUsuario("useradmin", "useradmin1234")){
                userJpaControl.create(userAdmin);
            }   
        }catch(Exception ex){
                Logger.getLogger(ORMManager.class.getName()).log(Level.SEVERE, null, ex);            
        }finally{
            closeEMF();
        }
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
