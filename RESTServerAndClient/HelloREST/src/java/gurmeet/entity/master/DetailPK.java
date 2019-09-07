package gurmeet.entity.master;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gurmeet Singh, gsdhillon@gmail.com
 */
@Embeddable
public class DetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "mid")
    private int mid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sno")
    private int sno;

    public DetailPK() {
    }

    public DetailPK(int mid, int sno) {
        this.mid = mid;
        this.sno = sno;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) mid;
        hash += (int) sno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailPK)) {
            return false;
        }
        DetailPK other = (DetailPK) object;
        if (this.mid != other.mid) {
            return false;
        }
        if (this.sno != other.sno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailPK[ mid=" + mid + ", sno=" + sno + " ]";
    }
}