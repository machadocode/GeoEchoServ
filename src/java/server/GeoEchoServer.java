/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package server;

import control.persistence.ORMManager;
import control.request.RequestManager;
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
import model.client.Message;
import model.client.Packet;
import model.client.QueryApp;
import model.client.QueryDesk;
import model.client.RegisterApp;
import model.client.Response;
import model.server.Session;

/**
 *  Classe principal del servidor GeoEcho que processa les peticions 
 * @author Dani Machado
 */
public class GeoEchoServer extends HttpServlet {
    ORMManager ormManager;
    SessionManager sessionManager;
    RequestManager requestManager;
    Packet packet;
    Session session;
    Response responseServ;

    /**
     * Constructor principal del servlet
     */
    public GeoEchoServer() {
        ormManager = new ORMManager();
        sessionManager = new SessionManager();
        requestManager = null;
        packet = null;
        session = null;
        responseServ = null;
    }   
               
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
        if (packet != null){
            requestManager = new RequestManager(ormManager, sessionManager);
            // Peticions LOGIN
            if(packet instanceof Login){
                if(packet instanceof LoginDesk){
                    responseServ = requestManager.executeLoginDesk((LoginDesk) packet);
                }else if (packet instanceof LoginApp){
                    responseServ = requestManager.executeLoginApp((LoginApp) packet);
                }            
            }
            // Peticions REGISTER APP
            if(packet instanceof RegisterApp){
                responseServ = requestManager.executeRegisterApp((RegisterApp) packet);
            }
            // Peticions LOGOUT
            if(packet instanceof Logout){
                responseServ = requestManager.executeLogout((Logout) packet);
            }
            // Peticions MESSAGE
            if(packet instanceof Message){
                responseServ = requestManager.executeMessage((Message) packet);
            }
            // Peticions QUERY APP
            if(packet instanceof QueryApp){
                responseServ = requestManager.executeQueryApp((QueryApp) packet);
            }
            // Peticions QUERY DESK
            if(packet instanceof QueryDesk){
                responseServ = requestManager.executeQueryDesk((QueryDesk) packet);
            }            
        }
  
        /**
         * Resposta del servidor a les peticions (Retorna un objecte Response del model)
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
