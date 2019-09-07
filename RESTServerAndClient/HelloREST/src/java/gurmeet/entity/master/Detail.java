package gurmeet.entity.master;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@Entity
@Table(name = "detail")
@NamedQueries({
    @NamedQuery(name = "Detail.findAll", query = "SELECT d FROM Detail d")
    , @NamedQuery(name = "Detail.findByMidSno", query = "SELECT d FROM Detail d WHERE d.detailPK.mid = :mid and d.detailPK.sno = :sno")
})
@XmlRootElement
public class Detail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailPK detailPK;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    
    
    @JoinColumn(name = "mid", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @XmlTransient
    private Master master;
    
    
    public Master getMaster() {
        return master;
    }
    
    public void setMaster(Master master) {
        this.master = master;
        //GURMEET to persist with master
//        this.detailPK.setMid(master.getId());
    }

    public Detail() {
    }

    public Detail(DetailPK detailPK) {
        this.detailPK = detailPK;
    }
    
    public Detail(int mid, int sno, String name) {
        this.detailPK = new DetailPK(mid, sno);
        this.name = name;
    }
//
//    public Detail(int mid, int sno) {
//        this.detailPK = new DetailPK(mid, sno);
//    }

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

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailPK != null ? detailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detail)) {
            return false;
        }
        Detail other = (Detail) object;
        if ((this.detailPK == null && other.detailPK != null) || (this.detailPK != null && !this.detailPK.equals(other.detailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Detail[ detailPK=" + detailPK + " ]";
    }
    
}
