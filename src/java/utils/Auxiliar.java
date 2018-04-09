/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import model.client.Message;
import model.client.User;
import model.server.MessageEntity;
import model.server.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe auxiliar amb mètodes i utilitats diverses
 * @author Dani Machado
 */
public final class Auxiliar {
    
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
}
