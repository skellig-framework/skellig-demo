package org.skellig.demo.app.rest.model;

import java.time.LocalDateTime;
import java.util.List;

public class Booking {

    private String eventCode;
    private List<String> seats;
    private float price;
    private LocalDateTime time;
    private String status;

    public Booking(String eventCode, List<String> seats, float price, LocalDateTime time,
                   String status) {
        this.eventCode = eventCode;
        this.seats = seats;
        this.price = price;
        this.time = time;
        this.status = status;
    }

    public String getEventCode() {
        return eventCode;
    }

    public List<String> getSeats() {
        return seats;
    }

    public float getPrice() {
        return price;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}