/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     * Processament de peticions en paquets HTTP tan GET com POST
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {   
        /**
         * Entrada de peticions al servidor
         */
        try(ObjectInputStream in = new ObjectInputStream(request.getInputStream())){
            packet = (Packet) in.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            Logger.getLogger(GeoEchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /**
         * Lògica del processament de peticions
         */                      
        if(packet instanceof  Login){
            responseServ = new Response();
            if(packet instanceof LoginDesk){
                session = sessionManager.createSession(ormManager, (LoginDesk) packet, true);
                responseServ.setSessionID(session.getSessionID());
            }else if (packet instanceof LoginApp){
                session = sessionManager.createSession(ormManager, (LoginApp) packet, false);                        
                responseServ.setSessionID(session.getSessionID());
            }            
        }
        if(packet instanceof RegisterApp){
            RegisterApp register = (RegisterApp) packet;
            responseServ = new Response();
            System.out.println("packet instanceof RegisterApp");
            if(ormManager.checkUserAvailable(register.getUser())){
                if(ormManager.checkEmailAvailable(register.getMail())){
                    if(ormManager.registerUser(register)){
                        session = new Session(true, sessionManager.createSessionId(register.getUser(), register.getPass()), register.getUser());
                        responseServ.setSessionID(session.getSessionID());
                    }                       
                }else{
                    responseServ.setSessionID(2);
                }
            }else{
                responseServ.setSessionID(1);
            } 
        }
        if(packet instanceof Logout){
            responseServ = new Response();
            sessionManager.logout((Logout) packet);
        }

        
        /**
         * Resposta del servidor a les peticions
         */
        try(ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream())){
            out.writeObject(responseServ);
        } catch (IOException ex) {
            Logger.getLogger(GeoEchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
