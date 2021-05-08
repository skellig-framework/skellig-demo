package org.skellig.demo.app.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
public class BookingEntity {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private String seats;
    @Column
    private float price;
    @Column
    private LocalDateTime time;
    @Column
    private BookingStatus status;
    @Column
    private String paymentToken;
    @ManyToOne(targetEntity = EventEntity.class)
    @JoinColumn(name = "code")
    private EventEntity event;

    public BookingEntity() {
        status = BookingStatus.PENDING;
        time = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long code) {
        this.id = code;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }
}