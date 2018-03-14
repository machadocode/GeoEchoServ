/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package control.persistence.jpa.exceptions;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe Exception del controlador del ORM (autogenerada per l'IDE)
 * @author Dani Machado
 */
public class IllegalOrphanException extends Exception {
    private List<String> messages;

    /**
     *
     * @param messages
     */
    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<String>();
        }
        else {
            this.messages = messages;
        }
    }

    /**
     *
     * @return
     */
    public List<String> getMessages() {
        return messages;
    }
}
