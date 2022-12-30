/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws.a.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.a.database.exceptions.NonexistentEntityException;
import ws.a.database.exceptions.PreexistingEntityException;

/**
 *
 * @author MADD
 */
public class StudentsJpaController implements Serializable {

    public StudentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ws.a_database_jar_0.0.1-SNAPSHOTPU");
    
    public StudentsJpaController(){
        
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Students students) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(students);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStudents(students.getId()) != null) {
                throw new PreexistingEntityException("Students " + students + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Students students) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            students = em.merge(students);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = students.getId();
                if (findStudents(id) == null) {
                    throw new NonexistentEntityException("The students with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Students students;
            try {
                students = em.getReference(Students.class, id);
                students.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The students with id " + id + " no longer exists.", enfe);
            }
            em.remove(students);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Students> findStudentsEntities() {
        return findStudentsEntities(true, -1, -1);
    }

    public List<Students> findStudentsEntities(int maxResults, int firstResult) {
        return findStudentsEntities(false, maxResults, firstResult);
    }

    private List<Students> findStudentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Students.class));
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

    public Students findStudents(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Students.class, id);
        } finally {
            em.close();
        }
    }

    public int getStudentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Students> rt = cq.from(Students.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
