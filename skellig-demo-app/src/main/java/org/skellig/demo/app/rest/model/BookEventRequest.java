package org.skellig.demo.app.rest.model;

import java.util.List;

public class BookEventRequest {

    private String eventCode;
    private List<String> seats;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }
}
