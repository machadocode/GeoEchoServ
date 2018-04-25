/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import model.client.Message;
import model.client.QueryApp;
import model.client.User;
import model.server.MessageEntity;
import model.server.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe auxiliar amb mètodes i utilitats diverses
 * @author Dani Machado
 */
public final class Auxiliar {
    public static final int MESSAGES_DISTANCE = 100000;
    
    /**
     * Mètode que tranforma una llista d'objectes MessageEntity (JPA persistència) en una altra Message (del model clients)
     * @param messageEntityList
     * @return Retorna la llista de Message
     */
    public static List<Message> convertMessageEntityList(List<MessageEntity> messageEntityList){
        List<Message> messageList = new ArrayList<>();
        for(MessageEntity messageEntity : messageEntityList){
            messageList.add(new Message(
                messageEntity.getCoordX(), 
                messageEntity.getCoordY(),
                messageEntity.getText(),
                messageEntity.getPhotoBase64(),
                messageEntity.getUserSender(),
                messageEntity.getUserReceiver(),
                messageEntity.getDateMsg(),
                messageEntity.getLife(),
                messageEntity.isMsgPublic(),
                messageEntity.isMsgVisible(),
                messageEntity.isMsgReaded()
            ));
        }        
        return messageList;
    }
    
    /**
     * Mètode que tranforma una llista d'objectes UserEntity (JPA persistència) en una altra User (del model clients)
     * @param userEntityList
     * @return Retorna la llista de User
     */
    public static List<User> convertUserEntityList(List<UserEntity> userEntityList){
        List<User> userList = new ArrayList<>();
        for(UserEntity userEntity : userEntityList){
            userList.add(new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.isAdminuser(),
                userEntity.isBanned()              
            ));
        }        
        return userList;
    }
    
    /**
     * Mètode que obtè un hash criptogràfic MD5 d'una cadena (password)
     * @param password
     * @return Retorna el hash criptogràfic
     */
    public static String encryptPasswordMD5(String password){
        return DigestUtils.md5Hex(password);
    }
    
    /**
     * Mètode que filtra els missatges retornant només els públics o els privats de l'usuari
     * @param messages Llista de missatges
     * @param user Usuari propietari o destinatari
     * @return Llista de missatges inclosos els privats filtrats
     */
    public static List<Message> publicPrivateMessagesFilter(List<Message> messages, String user){
        List<Message> filteredMessagesList = new ArrayList<>(); 
        for(Message message: messages){
            if(message.isMsgPublic() || message.getUserSender().equals(user) || message.getUserReceiver().equals(user)){
                filteredMessagesList.add(message);
            }       
        }
        return filteredMessagesList;
    }
    
    /**
     * Mètode que filtra els missatges públics situats a menys de 100 kms de la petició
     * @param queryApp
     * @param messages
     * @param distance
     * @return 
     */
    public static List<Message> publicDistanceMessagesFilter(QueryApp queryApp, List<Message> messages, int distance){
        List<Message> filteredMessagesList = new ArrayList<>(); 
        for(Message message: messages){
            if(message.isMsgPublic()){
                if(distFrom(queryApp.getCoordY(), queryApp.getCoordX(), message.getCoordY(),message.getCoordX()) < distance){
                    filteredMessagesList.add(message);                   
                }
            }       
        }
        return filteredMessagesList;
    }

    /**
     * Retorna la distància entre dos puntos
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return 
     */
    private static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //metres
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
}