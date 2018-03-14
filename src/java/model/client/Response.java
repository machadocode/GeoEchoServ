/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package model.client;

/**
 * Classe de resposta del servidor del model dades a empaquetar en les comunicacions
 * @author Dani Machado
 */
public class Response extends Packet{

    /**
     * Constant de LOGIN_FAILED
     */
    public static final int LOGIN_FAILED = 0;

    /**
     * Constant de LOGIN_OK
     */
    public static final int LOGIN_OK = 1;

    /**
     * Constant de REGISTER_FAILED
     */
    public static final int REGISTER_FAILED = 2;

    /**
     * Constant de REGISTER_NAME_FAILED
     */
    public static final int REGISTER_NAME_FAILED = 3;

    /**
     * Constant de REGISTER_EMAIL_FAILED
     */
    public static final int REGISTER_EMAIL_FAILED = 4;

    /**
     * Constant de REGISTER_OK
     */
    public static final int REGISTER_OK = 5;

    /**
     * Constant de LOGOUT_FAILED
     */
    public static final int LOGOUT_FAILED = 6;

    /**
     * Constant de LOGOUT_OK
     */
    public static final int LOGOUT_OK = 7;
    
    private int statusQuery;

    /**
     * Getter del satusQuery
     * @return retorna el codi d'estat de resposta
     */
    public int getStatusQuery() {
        return statusQuery;
    }

    /**
     * Setter del statusQuery
     * @param statusQuery Codi d'estat de resposta
     */
    public void setStatusQuery(int statusQuery) {
        this.statusQuery = statusQuery;
    }
}
