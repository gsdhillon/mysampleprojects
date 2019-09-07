package entity;

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
    private int mid;
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
    public String toString() {
        return "DetailPK[ mid=" + mid + ", sno=" + sno + " ]";
    }
    
}
