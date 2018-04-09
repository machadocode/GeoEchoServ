/*
 * App GeoEcho (Projecte final M13-DAM al IOC)
 * Copyright (c) 2018 - Papaya Team
 */
package control.persistence.jpa;

import control.persistence.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.server.MessageEntity;

/**
 * 
 * @author Dani Machado
 */
public class MessageEntityJpaController implements Serializable {

    public MessageEntityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MessageEntity messageEntity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(messageEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MessageEntity messageEntity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            messageEntity = em.merge(messageEntity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = messageEntity.getId();
                if (findMessageEntity(id) == null) {
                    throw new NonexistentEntityException("The messageEntity with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MessageEntity messageEntity;
            try {
                messageEntity = em.getReference(MessageEntity.class, id);
                messageEntity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The messageEntity with id " + id + " no longer exists.", enfe);
            }
            em.remove(messageEntity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MessageEntity> findMessageEntityEntities() {
        return findMessageEntityEntities(true, -1, -1);
    }

    public List<MessageEntity> findMessageEntityEntities(int maxResults, int firstResult) {
        return findMessageEntityEntities(false, maxResults, firstResult);
    }

    private List<MessageEntity> findMessageEntityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MessageEntity.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MessageEntity findMessageEntity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MessageEntity.class, id);
        } finally {
            em.close();
        }
    }

    public int getMessageEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MessageEntity> rt = cq.from(MessageEntity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
