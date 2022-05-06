package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "triptypes")
public class Triptype {
    @Id
    @Column(name = "TripTypeId", nullable = false, length = 1)
    private String id;

    @Column(name = "TTName", length = 25)
    private String tTName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTTName() {
        return tTName;
    }

    public void setTTName(String tTName) {
        this.tTName = tTName;
    }

}