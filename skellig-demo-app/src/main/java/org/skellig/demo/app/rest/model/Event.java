package org.skellig.demo.app.rest.model;

import java.time.LocalDateTime;

public class Event {
    
    private String code;
    private String name;
    private LocalDateTime date;
    private String location;

    public Event(String code, String name, LocalDateTime date, String location) {
        this.code = code;
        this.name = name;
        this.date = date;
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}