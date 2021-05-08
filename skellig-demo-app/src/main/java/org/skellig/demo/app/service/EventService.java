package org.skellig.demo.app.service;

import org.skellig.demo.app.event.handler.event.model.EventChangedEvent;

import java.util.List;
import java.util.Map;

public interface EventService {

    void changeEvent(EventChangedEvent event);

    void deleteEvent(String eventCode);

    void updateAvailableSeats(String eventCode, List<String> takenSeats);

    void releaseSeats(String eventCode, List<String> seats);

    Map<String, String> getGroupedPricesPerSeats(String pricesPerSeat);
}
