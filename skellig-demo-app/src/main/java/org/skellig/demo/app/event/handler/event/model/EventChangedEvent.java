package org.skellig.demo.app.event.handler.event.model;

import org.springframework.format.annotation.DateTimeFormat;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class EventChangedEvent {

    private String code;
    private String name;
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    private String location;
    private List<String> pricePerSeats;
    private List<String> takenSeats;

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

    public List<String> getPricePerSeats() {
        return pricePerSeats;
    }

    public void setPricePerSeats(List<String> pricePerSeats) {
        this.pricePerSeats = pricePerSeats;
    }

    public List<String> getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(List<String> takenSeats) {
        this.takenSeats = takenSeats;
    }
}
