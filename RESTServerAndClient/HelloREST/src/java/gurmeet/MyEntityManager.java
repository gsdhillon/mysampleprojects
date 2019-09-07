package gurmeet;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
//import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
public class MyEntityManager {
    @PersistenceUnit
    EntityManagerFactory emf;
    
//    @PersistenceContext
    private EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    protected void open() throws Exception{
        em = emf.createEntityManager();
    }
    
    protected void beginTransaction() throws Exception{
        em = emf.createEntityManager();
        utx.begin();
        em.joinTransaction();
        System.out.println("UserTransaction begin");
    }

    protected void commitTransaction() throws Exception{
        utx.commit();
        System.out.println("UserTransaction commit");
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    protected void rollbackTransaction(){
        try{
            utx.rollback();
            System.out.println("UserTransaction rollback");
        }catch(IllegalStateException | SecurityException | SystemException e){
            e.printStackTrace();
        }
    }
    
    protected Query createNamedQuery(String jpaQuery) throws Exception{
        return em.createNamedQuery(jpaQuery);
    }
    
    protected void persist(Object o) throws Exception{
        em.persist(o);
    }
    
    protected void merge(Object o) throws Exception{
        em.merge(o);
    }
    
    
    protected void remove(Object o) throws Exception{
        em.remove(o);
    }
    
    protected void flush() throws Exception{
        em.flush();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    protected void close() {
        try {
            if (em != null) {
                em.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
