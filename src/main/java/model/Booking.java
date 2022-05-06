package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingId", nullable = false)
    private Integer id;

    @Column(name = "BookingDate")
    private Date bookingDate;

    @Column(name = "BookingNo", length = 50)
    private String bookingNo;

    @Column(name = "BookingTotal")
    private Double bookingTotal;

    @Column(name = "TravelerCount")
    private Double travelerCount;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CustomerId")
    private Integer customer;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TripTypeId")
    private String tripType;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "PackageId")
    private Integer _package;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public Double getBookingTotal() {
        return bookingTotal;
    }

    public void setBookingTotal(Double bookingTotal) {
        this.bookingTotal = bookingTotal;
    }

    public Double getTravelerCount() {
        return travelerCount;
    }

    public void setTravelerCount(Double travelerCount) {
        this.travelerCount = travelerCount;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Integer get_package() {
        return _package;
    }

    public void set_package(Integer _package) {
        this._package = _package;
    }

}