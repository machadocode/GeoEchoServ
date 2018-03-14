/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package control.persistence.jpa.exceptions;
/**
 * Classe Exception del controlador del ORM (autogenerada per l'IDE)
 * @author Dani Machado
 */
public class NonexistentEntityException extends Exception {

    /**
     *
     * @param message
     * @param cause
     */
    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     */
    public NonexistentEntityException(String message) {
        super(message);
    }
}
