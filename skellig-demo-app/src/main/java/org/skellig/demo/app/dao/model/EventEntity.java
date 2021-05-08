package org.skellig.demo.app.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Event")
public class EventEntity {

    @Id
    private String code;
    @Column
    private String name;
    @Column
    private LocalDateTime date;
    @Column
    private String location;
    @Column
    private String pricePerSeats;
    @Column
    private String takenSeats;
    @OneToMany(mappedBy = "event", targetEntity = BookingEntity.class)
    private List<BookingEntity> bookings;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPricePerSeats() {
        return pricePerSeats == null ? "" : pricePerSeats;
    }

    public void setPricePerSeats(String pricePerSeats) {
        this.pricePerSeats = pricePerSeats;
    }

    public String getTakenSeats() {
        return takenSeats == null ? "" : takenSeats;
    }

    public void setTakenSeats(String takenSeats) {
        this.takenSeats = takenSeats;
    }

    public List<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
    }
}