package gurmeet.dao;

import gurmeet.MyEntityManager;
import gurmeet.entity.master.Detail;
import gurmeet.entity.master.Master;
import java.util.List;
//import javax.inject.Singleton;
import javax.enterprise.context.RequestScoped;
/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@SuppressWarnings("CallToPrintStackTrace")
//@Singleton
@RequestScoped
public class MasterDao extends MyEntityManager{

    public MasterDao() {
        System.out.println("MasterDao created");
    }
    
    public String create(Master master) {
        try {
            beginTransaction();
            //
            System.out.println("going to create - "+master);
            List<Detail> detailList = master.getDetailList();
            master.setDetailList(null);
            persist(master);
            flush();//to get id in master
            //
            if (detailList != null) {
                detailList.stream().forEach((detail) -> {
                    detail.setMaster(master);
                    detail.getDetailPK().setMid(master.getId());
                    //System.out.println(detail);
                });
            }
            master.setDetailList(detailList);
            merge(master);
            //
            commitTransaction();
            return "OK:" + master.getId();
        } catch (Exception e) {
            e.printStackTrace();
            rollbackTransaction();
            return "FAILED: Master creation failed.";
        } finally {
            close();
        }
    }
    
    public String update(Master master) {
        try {
            beginTransaction();
            //link all details to master
            List<Detail> detailList = master.getDetailList();
            if (detailList != null) {
                for (Detail detail : detailList) {
                    detail.setMaster(master);
                    detail.getDetailPK().setMid(master.getId());
                    merge(detail);
                }
            }
            //
            merge(master);
            flush();
            //
            commitTransaction();
            return "OK:" + master.getId();
        } catch (Exception e) {
            e.printStackTrace();
            rollbackTransaction();
            return "FAILED: Master creation failed.";
        } finally {
            close();
        }
    }
 
     public Master find(Integer id) {
        try {
            open();
            Master master = (Master) createNamedQuery("Master.findById").setParameter("id", id).getSingleResult();
            System.out.println(master);
            List<Detail> detailList = master.getDetailList();
            if (detailList != null) {
                for (Detail detail : detailList) {
                    detail.setMaster(null);
                }
            }
            return master;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    
    public List<Master> findAll() {
        try {
            open();
            List<Master> masters = createNamedQuery("Master.findAll").getResultList();
            
            if(masters != null) masters.stream().forEach(master -> {
                System.out.println(master);
                List<Detail> detailList = master.getDetailList();
                if(detailList != null) detailList.stream().forEach(detail ->{
                    detail.setMaster(null);
                });
            });
            return masters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public String remove(Integer id) {
        try {
            beginTransaction();
            Master existing = (Master) createNamedQuery("Master.findById").setParameter("id", id).getSingleResult();
            if (existing == null) {
                return "FAILED: no record found with id = "+id;
            }
            System.out.println("going to remove - " + existing);
            
            //link all details to master
            int detailCount = 0;
            List<Detail> detailList = existing.getDetailList();
            if (detailList != null) {
                for (Detail detail : detailList) {
                    detail.setMaster(existing);
                    detail.getDetailPK().setMid(existing.getId());
                    remove(detail);
                    detailCount++; 
                }
            }
            remove(existing);
            //System.out.println("removed - " + existing);
            //
            commitTransaction();
            return "OK: removed master with id = "+id+" and "+detailCount+" details";
        } catch (Exception e) {
           e.printStackTrace();
           rollbackTransaction();
           return null;
        } finally {
            close();
        }
    }
}