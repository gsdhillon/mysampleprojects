package entity;

import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@XmlRootElement
public class Detail {
    private DetailPK detailPK;
    private String name;
   
    public Detail(){
    }

    public Detail(int sno, String name) {
        this.detailPK = new DetailPK(0, sno);
        this.name = name;
    }
    
    public Detail(int mid, int sno, String name) {
        this.detailPK = new DetailPK(mid, sno);
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" + detailPK + ", name=" + name + '}';
    }

    public DetailPK getDetailPK() {
        return detailPK;
    }

    public void setDetailPK(DetailPK detailPK) {
        this.detailPK = detailPK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}