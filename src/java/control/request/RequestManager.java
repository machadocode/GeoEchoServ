/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */

package control.request;

import control.persistence.ORMManager;
import control.session.SessionManager;
import java.util.ArrayList;
import java.util.List;
import model.client.LoginApp;
import model.client.LoginDesk;
import model.client.Logout;
import model.client.Message;
import model.client.QueryApp;
import model.client.QueryDesk;
import model.client.RegisterApp;
import model.client.Response;
import model.client.ResponseQuery;
import model.client.ResponseQueryApp;
import model.client.ResponseQueryDesk;
import model.client.UpdateUser;
import model.client.User;
import model.server.MessageEntity;
import model.server.Session;
import model.server.UserEntity;
import utils.Auxiliar;

/**
 * Classe que processa i conté la lògica de les peticions del client al servidor
 * @author Dani Machado
 */
public final class RequestManager {
    
    private final ORMManager ormManager;
    private final SessionManager sessionManager;
    private Session session;
    private final Response response;
    
    /**
     * Constructor principal de la classe
     * @param ormManager
     * @param sessionManager
     */
    public RequestManager(ORMManager ormManager, SessionManager sessionManager) {
        this.ormManager = ormManager;
        this.sessionManager = sessionManager;
        this.response = new Response();
        this.session = null;
    }
    
    /**
     * Processa la petició LoginDesk
     * @param loginDesk
     * @return Retorna Response
     */
    public Response executeLoginDesk(LoginDesk loginDesk){
        session = sessionManager.createSession(ormManager, loginDesk, true);
        if (session.getSessionID() != 0){
            response.setSessionID(session.getSessionID());
            response.setStatusQuery(Response.LOGIN_OK);
        }else{
            response.setStatusQuery(Response.LOGIN_FAILED);                    
        }
        return response;
    }

    /**
     * Processa la petició LoginApp
     * @param loginApp
     * @return Retorna Response
     */
    public Response executeLoginApp(LoginApp loginApp){
        session = sessionManager.createSession(ormManager, loginApp, false);
        if (session.getSessionID() != 0){
            response.setSessionID(session.getSessionID());
            response.setStatusQuery(Response.LOGIN_OK);
        }else{
            response.setStatusQuery(Response.LOGIN_FAILED);                    
        }        
        return response; 
    }
    
    /**
     * Processa petició RegisterApp
     * @param registerApp
     * @return Retorna Response
     */
    public Response executeRegisterApp(RegisterApp registerApp){
        if(ormManager.checkUserAvailable(registerApp.getUser())){
            if(ormManager.checkEmailAvailable(registerApp.getMail())){
                if(ormManager.registerUser(registerApp)){
                    session = new Session(true, sessionManager.createSessionId(registerApp.getUser(), registerApp.getPass()), registerApp.getUser(), false);
                    response.setSessionID(session.getSessionID());
                    if (session.getSessionID() != 0){
                        response.setStatusQuery(Response.REGISTER_OK);
                    }else{
                        response.setStatusQuery(Response.REGISTER_FAILED);                    
                    }
                }                       
            }else{
                response.setStatusQuery(Response.REGISTER_EMAIL_FAILED); 
            }
        }else{
            response.setStatusQuery(Response.REGISTER_NAME_FAILED);
        } 
        return response;
    }

    /**
     * Processa petició Logout
     * @param logout
     * @return Retorna Response
     */
    public Response executeLogout(Logout logout){
        if (sessionManager.logout(logout)){
            response.setStatusQuery(Response.LOGOUT_OK);
        }else{
            response.setStatusQuery(Response.LOGOUT_FAILED);                    
        }        
        return response;    
    }
    
    /**
     * Processa petició Message
     * @param message
     * @return Retorna Response
     */
    public Response executeMessage(Message message){
        if(sessionManager.checkSession(message)){
            response.setSessionID(message.getSessionID());
            MessageEntity messageEntity = new MessageEntity(message);
            if(ormManager.registerMessage(messageEntity)){
                response.setStatusQuery(Response.MESSAGE_OK);
            }else{
                response.setStatusQuery(Response.MESSAGE_FAILED);                
            }
        }else{
            response.setStatusQuery(Response.SESSION_FAILED);            
        }    
        return response;
    }
    
    /**
     * Processa petició QueryApp
     * @param queryApp
     * @return Retorna Response
     */
    public ResponseQuery executeQueryApp(QueryApp queryApp){
        List<MessageEntity> messageEntityList;
        List<Message> messageList;
        List<Message> nearMessagesList;
        ResponseQueryApp responseQueryApp = new ResponseQueryApp();

        if(sessionManager.checkSession(queryApp)){
            messageEntityList = ormManager.getAllMessages();
            // Transforma la llista de MessageEntity del model server a Message del model client
            messageList = Auxiliar.convertMessageEntityList(messageEntityList);
            // Filtra els missatges per tots els públics i els privats de l'usuari que fa la petició
            messageList = Auxiliar.publicPrivateMessagesFilter(messageList, sessionManager.getPacketSession(queryApp).getUser());
            // Filtra els missatges propers < 100 km
            nearMessagesList = Auxiliar.publicDistanceMessagesFilter(queryApp, messageList, Auxiliar.MESSAGES_DISTANCE);

            responseQueryApp.setSessionID(queryApp.getSessionID());

            if(messageList.size() > 0){
                responseQueryApp.setStatusQuery(Response.REQUEST_OK);        
            }else{
                responseQueryApp.setStatusQuery(Response.REQUEST_FAILED);                    
            }

            responseQueryApp.setMessageList(nearMessagesList);

        }else{
            responseQueryApp.setStatusQuery(Response.SESSION_FAILED);            
        }    
        return responseQueryApp;

    }

    /**
     * Processa petició QueryDesk
     * @param queryDesk
     * @return Retorna Response
     */
    public Response executeQueryDesk(QueryDesk queryDesk){
        List<MessageEntity> messageEntityList;
        List<Message> messageList;
        List<UserEntity> userEntityList = new ArrayList<>();
        List<User> userList;        
        ResponseQueryDesk responseQueryDesk = new ResponseQueryDesk();

        if(sessionManager.checkSession(queryDesk)){
            if(queryDesk.getUsername().equals(QueryDesk.ALL)){
                messageEntityList = ormManager.getAllMessages();
                messageList = Auxiliar.convertMessageEntityList(messageEntityList);            
                userEntityList = ormManager.getAllUsers();
                userList = Auxiliar.convertUserEntityList(userEntityList);
            }else{
                messageEntityList = ormManager.getMessagesFromUser(queryDesk.getUsername());
                messageList = Auxiliar.convertMessageEntityList(messageEntityList);
                userEntityList.add(ormManager.getUser(queryDesk.getUsername()));
                userList = Auxiliar.convertUserEntityList(userEntityList);            
            }
            responseQueryDesk.setSessionID(queryDesk.getSessionID());

            if(messageList.size() > 0 && userList.size() > 0){
                responseQueryDesk.setStatusQuery(Response.REQUEST_OK);        
            }else{
                responseQueryDesk.setStatusQuery(Response.REQUEST_FAILED);                    
            }

            responseQueryDesk.setMessageList(messageList);
            responseQueryDesk.setUserList(userList);

            return responseQueryDesk;            
        }else{
            responseQueryDesk.setStatusQuery(Response.SESSION_FAILED);            
        }    
        return responseQueryDesk;       

    }
    
    /**
     * Processa petició UpdateUser
     * @param updateUser
     * @return Retorna Response
     */
    public Response executeUpdateUser(UpdateUser updateUser){
        if(sessionManager.checkSessionAdmin(updateUser) && ormManager.updateUser(updateUser)){
           response.setStatusQuery(Response.UPDATE_USER_OK);
           response.setSessionID(updateUser.getSessionID());
        }else{
            response.setStatusQuery(Response.UPDATE_USER_FAILED);
        }   
        return response;
    } 
}
