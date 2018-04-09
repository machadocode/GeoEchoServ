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
import model.client.User;
import model.server.MessageEntity;
import model.server.Session;
import model.server.UserEntity;
import utils.Auxiliar;

/**
 * 
 * @author Dani Machado
 */
public final class RequestManager {
    
    private final ORMManager ormManager;
    private final SessionManager sessionManager;
    private Session session;
    private final Response response;
    
    public RequestManager(ORMManager ormManager, SessionManager sessionManager) {
        this.ormManager = ormManager;
        this.sessionManager = sessionManager;
        this.response = new Response();
        this.session = null;
    }
    
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

    public Response executeLoginApp(LoginApp loginApp){
        session = sessionManager.createSession(ormManager, (LoginApp) loginApp, false);
        if (session.getSessionID() != 0){
            response.setSessionID(session.getSessionID());
            response.setStatusQuery(Response.LOGIN_OK);
        }else{
            response.setStatusQuery(Response.LOGIN_FAILED);                    
        }        
        return response; 
    }
    
    public Response executeRegisterApp(RegisterApp registerApp){
        if(ormManager.checkUserAvailable(registerApp.getUser())){
            if(ormManager.checkEmailAvailable(registerApp.getMail())){
                if(ormManager.registerUser(registerApp)){
                    session = new Session(true, sessionManager.createSessionId(registerApp.getUser(), registerApp.getPass()), registerApp.getUser());
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

    public Response executeLogout(Logout logout){
        if (sessionManager.logout(logout)){
            response.setStatusQuery(Response.LOGOUT_OK);
        }else{
            response.setStatusQuery(Response.LOGOUT_FAILED);                    
        }        
        return response;    
    }
    
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
    
    public ResponseQuery executeQueryApp(QueryApp queryApp){
        List<MessageEntity> messageEntityList;
        List<Message> messageList;
        List<Message> nearMessagesList;        
        ResponseQueryApp responseQueryApp = new ResponseQueryApp();

        if(sessionManager.checkSession(queryApp)){
            messageEntityList = ormManager.getAllMessages();
            messageList = Auxiliar.convertMessageEntityList(messageEntityList);
           /*
            * Lògica per retornar només els missatges propers > 10 km
            */
            nearMessagesList = messageList;

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
}
