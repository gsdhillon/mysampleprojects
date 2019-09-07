package entity;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@XmlRootElement
public class Master {
    private Integer id = null;
    private String name = null;
    @XmlElement
    private List<Detail> detailList;

    public Master(){
    }

    public Master(String name) {
        this.name = name;
    }
    
    public Master(Integer userId, String name) {
        this.id = userId;
        this.name = name;
    }

    @Override
    public String toString() {
        String d = (detailList==null?"null":"detailList size - "+detailList.size());
        return "Master{" + "id=" + id + ", name=" + name + "["+d+"]" + '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer userId) {
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Detail> detailList) {
        this.detailList = detailList;
    }
    
  }