/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package server;

import control.persistence.ORMManager;
import control.session.SessionManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.client.Login;
import model.client.LoginApp;
import model.client.LoginDesk;
import model.client.Logout;
import model.client.Packet;
import model.client.RegisterApp;
import model.client.Response;
import model.server.Session;

/**
 *  Classe principal del servidor GeoEcho que processa les peticions 
 * @author Dani Machado
 */
public class GeoEchoServer extends HttpServlet {
        ORMManager ormManager = new ORMManager();
        SessionManager sessionManager = new SessionManager();
        Packet packet = null;
        Session session = null;
        Response responseServ = null;
        
    /**
     * Processament de peticions al servlet en paquets HTTP tan GET com POST
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {   
        /**
         * Entrada de peticions al servidor. Rep un objecte Packet del model de comunicacions
         */
        try(ObjectInputStream in = new ObjectInputStream(request.getInputStream())){
            packet = (Packet) in.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(GeoEchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /**
         * Lògica del processament de peticions
         */    
        // Peticions LOGIN
        if(packet instanceof  Login){
            responseServ = new Response();
            if(packet instanceof LoginDesk){
                session = sessionManager.createSession(ormManager, (LoginDesk) packet, true);
                responseServ.setSessionID(session.getSessionID());
                if (session.getSessionID() != 0){
                    responseServ.setStatusQuery(Response.LOGIN_OK);
                }else{
                    responseServ.setStatusQuery(Response.LOGIN_FAILED);                    
                }
            }else if (packet instanceof LoginApp){
                session = sessionManager.createSession(ormManager, (LoginApp) packet, false);                        
                responseServ.setSessionID(session.getSessionID());
                if (session.getSessionID() != 0){
                    responseServ.setStatusQuery(Response.LOGIN_OK);
                }else{
                    responseServ.setStatusQuery(Response.LOGIN_FAILED);                    
                }
            }            
        }
        // Peticions REGISTER APP
        if(packet instanceof RegisterApp){
            RegisterApp register = (RegisterApp) packet;
            responseServ = new Response();
            if(ormManager.checkUserAvailable(register.getUser())){
                if(ormManager.checkEmailAvailable(register.getMail())){
                    if(ormManager.registerUser(register)){
                        session = new Session(true, sessionManager.createSessionId(register.getUser(), register.getPass()), register.getUser());
                        responseServ.setSessionID(session.getSessionID());
                        if (session.getSessionID() != 0){
                            responseServ.setStatusQuery(Response.REGISTER_OK);
                        }else{
                            responseServ.setStatusQuery(Response.REGISTER_FAILED);                    
                        }
                    }                       
                }else{
                    responseServ.setSessionID(2);   // Provisional per gestionar la resposta abans d'implementar els codis de status query de Response
                    responseServ.setStatusQuery(Response.REGISTER_EMAIL_FAILED);    // Definitiu per gestionar la resposta amb els codis de Response
                }
            }else{
                responseServ.setSessionID(1);   // Provisional per controlar la resposta abans d'implementar els codis de status query de Response
                responseServ.setStatusQuery(Response.REGISTER_NAME_FAILED);     // Definitiu per gestionar la resposta amb els codis de Response
            } 
        }
        // Peticions LOGOUT
        if(packet instanceof Logout){
            responseServ = new Response();
            if (sessionManager.logout((Logout) packet)){
                responseServ.setStatusQuery(Response.LOGOUT_OK);
            }else{
                responseServ.setStatusQuery(Response.LOGOUT_FAILED);                    
            }
        }
        
        /**
         * Resposta del servidor a les peticions (Retorna un objecte Response del model de comunicacions)
         */
        try(ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream())){
            out.writeObject(responseServ);
        } catch (IOException ex) {
            Logger.getLogger(GeoEchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Processament de peticions al servlet en paquets HTTP del tipus GET
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processament de peticions al servlet en paquets HTTP del tipus POST
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Retorna una curta descripció del servlet.
     *
     * @return un String amb la descripció del servlet
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
