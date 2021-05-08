package org.skellig.demo.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skellig.demo.app.dao.EventChangedLogDao;
import org.skellig.demo.app.dao.EventDao;
import org.skellig.demo.app.dao.model.EventChangedLog;
import org.skellig.demo.app.dao.model.EventEntity;
import org.skellig.demo.app.event.handler.event.model.EventChangedEvent;
import org.skellig.demo.app.exception.SkelligServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EventServiceImpl implements EventService {

    private EventDao eventDao;
    private EventChangedLogDao eventChangedLogDao;
    private ObjectMapper objectMapper;

    @Autowired
    public EventServiceImpl(EventDao eventDao, EventChangedLogDao eventChangedLogDao) {
        this.eventDao = eventDao;
        this.eventChangedLogDao = eventChangedLogDao;
        objectMapper = new ObjectMapper();
    }

    @Transactional
    @Override
    public void changeEvent(EventChangedEvent event) {
        EventEntity eventEntity = eventDao.findById(event.getCode()).orElse(new EventEntity());
        eventEntity.setCode(event.getCode());
        eventEntity.setName(event.getName());
        eventEntity.setDate(event.getDate());
        eventEntity.setLocation(event.getLocation());
        eventEntity.setTakenSeats(getTakenUpdatedSeats(eventEntity.getTakenSeats(), event.getTakenSeats()));
        eventEntity.setPricePerSeats(String.join(",", event.getPricePerSeats()));

        eventDao.save(eventEntity);

        saveEventChangedLog(eventEntity);
    }

    @Transactional
    @Override
    public void deleteEvent(String eventCode) {
        eventDao.findById(eventCode).ifPresent(value -> eventDao.delete(value));
    }

    @Transactional
    @Override
    public void updateAvailableSeats(String eventCode, List<String> seatsToOccupy) {
        eventDao.findById(eventCode).ifPresent(event -> {
            Set<String> takenSeats = Stream.of(event.getTakenSeats().split(","))
                    .filter(seatsToOccupy::contains)
                    .collect(Collectors.toSet());
            if (!takenSeats.isEmpty()) {
                throw new SkelligServiceException("Can't occupy already taken seats: " + takenSeats);
            } else {
                Map<String, String> pricesPerSeat = getGroupedPricesPerSeats(event.getPricePerSeats());
                List<String> nonExistingSeats = seatsToOccupy.stream()
                        .filter(seat -> !pricesPerSeat.containsKey(seat))
                        .collect(Collectors.toList());
                if (nonExistingSeats.isEmpty()) {
                    event.setTakenSeats(getTakenUpdatedSeats(event.getTakenSeats(), seatsToOccupy));
                    eventDao.save(event);
                    saveEventChangedLog(event);
                } else {
                    throw new SkelligServiceException(String.format("Can't book seats %s which not exist in the event %s",
                            nonExistingSeats, event.getCode()));
                }
            }
        });
    }

    @Override
    public void releaseSeats(String eventCode, List<String> seats) {
        eventDao.findById(eventCode).ifPresent(event -> {
            String updatedSeats = Stream.of(event.getTakenSeats().split(","))
                    .filter(item -> !seats.contains(item))
                    .collect(Collectors.joining(","));
            event.setTakenSeats(updatedSeats);
            eventDao.save(event);
            saveEventChangedLog(event);
        });
    }

    @Override
    public Map<String, String> getGroupedPricesPerSeats(String pricesPerSeat) {
        return Stream.of(pricesPerSeat.split(","))
                .map(item -> Stream.of(item.split("=")).toArray())
                .filter(item -> item.length == 2)
                .collect(Collectors.toMap(i -> i[0].toString(), i -> i[1].toString()));
    }

    private String getTakenUpdatedSeats(String currentTakenSeats, Collection<String> newSeatsToOccupy) {
        Set<String> takenSeats =
                Stream.concat(Stream.of(currentTakenSeats.split(",")), newSeatsToOccupy.stream())
                        .collect(Collectors.toCollection(LinkedHashSet::new));
        return String.join(",", takenSeats);
    }

    private void saveEventChangedLog(EventEntity eventEntity) {
        EventChangedLog log = new EventChangedLog();
        try {
            log.setAfter(objectMapper.writeValueAsString(eventEntity));
        } catch (JsonProcessingException e) {
            log.setAfter("invalid");
        }
        eventChangedLogDao.save(log);
    }
}
