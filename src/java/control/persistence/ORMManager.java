/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package control.persistence;

import control.persistence.jpa.UserJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.client.RegisterApp;
import model.server.UserEntity;

/**
 * Classe d'alt nivell gestiona la persistència de dades mitjançant el mapatge ORM
 * @author Dani Machado
 */
public final class ORMManager {
    
    private EntityManagerFactory emf;
    private List<UserEntity> users;

    /**
     * Constructor principal del ORMManager
     */
    public ORMManager() {
        init();
        updateDefaultUsers();
    }
    
    /**
     * Inicia l'actualització del mapatge
     */
    private void init() {
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        users = userJpaControl.findUserEntities();
        closeEMF();     
    }
    
    /**
     * Crea l'EntityManagerFactory
     */
    private void createEMF(){
        emf =  Persistence.createEntityManagerFactory("geoechoservPU");      
    }

    /**
     * Tanca l'EntityManagerFactory
     */
    private void closeEMF(){
        emf.close();        
    }
    
    /**
     * Mètode que crea i persisteix els usuaris per defecte del sistema (a l'inici)
     */
    private void updateDefaultUsers(){
        boolean updated = false;
        createEMF();
        UserJpaController userJpaControl = new UserJpaController(emf);
        try{
            if(!checkUser("user", "user1234", false)){
                UserEntity userNormal = new UserEntity("user", "user1234", "user@gmail.com", false, false);
                userJpaControl.create(userNormal);
                updated = true;
            }
            if(!checkUser("admin", "admin1234", true)){
                UserEntity userAdmin = new UserEntity("admin", "admin1234", "useradmin@gmail.com", true, false);
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
    
    /**
     * Comprova si l'usuari està registrat al sistema
     * @param name nom d'usuari
     * @param password password d'usuari
     * @param admin rol administrador
     * @return resultat comprovació
     */
    public boolean checkUser(String name, String password, boolean admin){
        for (UserEntity user : users){
            if(user.getUsername().equals(name) && user.getPassword().equals(password) && user.isAdminuser() == admin){
                return true;
            }            
        }
        return false;
    }
    
    /**
     * Comprova si el nom d'usuari està disponible (restricció del nom d'usuari únic)
     * @param name nom a comrpovar
     * @return resultat comprovació
     */
    public boolean checkUserAvailable(String name){
        for (UserEntity user : users){
            if(user.getUsername().equals(name)){
                return false;
            }            
        }
        return true;        
    }

    /**
     * Comprova si l'email d'usuari està disponible (restricció d'email d'usuari únic)
     * @param email email a comprovar
     * @return resultat comprovació
     */
    public boolean checkEmailAvailable(String email){
        for (UserEntity user : users){
            if(user.getEmail().equals(email)){
                return false;
            }            
        }
        return true;        
    }
    
    /**
     * Registra un usuari al sistema dotant-lo de persistència a la base de dades
     * @param register Objecte register amb els atributs necessaris per registrar l'usuari
     * @return resultat del registre (true si ha tingut èxit)
     */
    public boolean registerUser(RegisterApp register){
        if(checkUserAvailable(register.getUser())){
            if(checkEmailAvailable(register.getMail())){
                createEMF();
                UserJpaController userJpaControl = new UserJpaController(emf);
                UserEntity user = new UserEntity(register.getUser(), register.getPass(), register.getMail(), false, false);
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
