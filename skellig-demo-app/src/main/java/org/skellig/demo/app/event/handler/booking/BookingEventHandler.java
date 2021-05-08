package org.skellig.demo.app.event.handler.booking;

import org.skellig.demo.app.event.handler.booking.model.BookingConfirmedEvent;
import org.skellig.demo.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingEventHandler {

    private final BookingService bookingService;
    private JmsTemplate jmsTemplate;

    @Autowired
    public BookingEventHandler(BookingService bookingService, JmsTemplate jmsTemplate) {
        this.bookingService = bookingService;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "DEV.QUEUE.1")
    public void bookingConfirmed(BookingConfirmedEvent event) {
        // check the booking and token and change the status in DB
        System.out.println(event);
        bookingService.confirmBooking(event.getBookingCode(), event.getPaymentProviderToken());
        jmsTemplate.convertAndSend("DEV.QUEUE.2", event);
    }
}
