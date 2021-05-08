package org.skellig.demo.app.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.skellig.demo.app.dao.BookingDao;
import org.skellig.demo.app.dao.EventDao;
import org.skellig.demo.app.dao.model.BookingEntity;
import org.skellig.demo.app.dao.model.BookingStatus;
import org.skellig.demo.app.dao.model.EventEntity;
import org.skellig.demo.app.exception.SkelligServiceException;
import org.skellig.demo.app.rest.model.BookEventRequest;
import org.skellig.demo.app.rest.model.BookEventResponse;
import org.skellig.demo.app.rest.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingServiceImpl implements BookingService {

    private BookingDao bookingDao;
    private EventService eventService;
    private EventDao eventDao;
    private ReportService reportService;

    @Autowired
    public BookingServiceImpl(BookingDao bookingDao, EventService eventService, EventDao eventDao) {
        this.bookingDao = bookingDao;
        this.eventService = eventService;
        this.eventDao = eventDao;
    }

    @Transactional
    @Override
    public BookEventResponse bookEvent(BookEventRequest bookEventRequest) {
        EventEntity event =
                eventDao.findById(bookEventRequest.getEventCode())
                        .orElseThrow(() -> new SkelligServiceException(String.format("Event with code %s does not exist", bookEventRequest.getEventCode())));

        eventService.updateAvailableSeats(bookEventRequest.getEventCode(), bookEventRequest.getSeats());

        Map<String, String> pricesPerSeat = eventService.getGroupedPricesPerSeats(event.getPricePerSeats());
        float price = (float) bookEventRequest.getSeats().stream()
                .map(pricesPerSeat::get)
                .filter(Objects::nonNull)
                .mapToDouble(Float::parseFloat)
                .sum();

        BookingEntity entity = new BookingEntity();
        entity.setSeats(String.join(",", bookEventRequest.getSeats()));
        entity.setPrice(price);
        entity.setEvent(event);
        entity.setPaymentToken(RandomStringUtils.random(10,true,true));

        bookingDao.save(entity);

        BookEventResponse bookEventResponse = new BookEventResponse();
        bookEventResponse.setBookingCode(entity.getId());
        bookEventResponse.setEventCode(bookEventRequest.getEventCode());
        bookEventResponse.setPaymentToken(entity.getPaymentToken());

        return bookEventResponse;
    }

    @Transactional
    @Override
    public void confirmBooking(long bookingCode, String paymentProviderToken) {
        BookingEntity bookingEntity = getBookingEntity(bookingCode);
        if (bookingEntity.getPaymentToken().equals(paymentProviderToken)) {
            bookingEntity.setStatus(BookingStatus.CONFIRMED);
            bookingDao.save(bookingEntity);
        } else {
            throw new SkelligServiceException("Invalid payment for the booking " + bookingCode);
        }
    }

    @Transactional
    @Override
    public void cancelBooking(long bookingCode) {
        BookingEntity bookingEntity = getBookingEntity(bookingCode);
        bookingDao.deleteById(bookingCode);
        eventService.releaseSeats(bookingEntity.getEvent().getCode(),
                Stream.of(bookingEntity.getSeats().split(",")).collect(Collectors.toList()));
    }

    @Override
    public Collection<Booking> getAll() {
        Collection<Booking> bookings = new ArrayList<>();
        bookingDao.findAll()
                .forEach(item -> bookings.add(convertToBooking(item)));
        return bookings;
    }

    private Booking convertToBooking(BookingEntity item) {
        List<String> seats = Stream.of(item.getSeats().split(",")).collect(Collectors.toList());
        return new Booking(item.getEvent().getCode(), seats, item.getPrice(), item.getTime(), item.getStatus().name());
    }

    @Override
    public Booking getByBookingCode(long bookingCode) {
        return bookingDao.findById(bookingCode).map(this::convertToBooking).orElse(null);
    }

    private BookingEntity getBookingEntity(long bookingCode) {
        return bookingDao.findById(bookingCode)
                .orElseThrow(() -> new SkelligServiceException("Booking not found for code: " + bookingCode));
    }

}
