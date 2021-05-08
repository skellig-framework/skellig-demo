package org.skellig.demo.app.service;

import org.skellig.demo.app.rest.model.BookEventRequest;
import org.skellig.demo.app.rest.model.BookEventResponse;
import org.skellig.demo.app.rest.model.Booking;

import java.util.Collection;

public interface BookingService {

    BookEventResponse bookEvent(BookEventRequest bookEventRequest);

    void confirmBooking(long bookingCode, String paymentProviderToken);

    void cancelBooking(long bookingCode);

    Collection<Booking> getAll();

    Booking getByBookingCode(long bookingCode);
}
