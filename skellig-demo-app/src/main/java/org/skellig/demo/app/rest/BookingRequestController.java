package org.skellig.demo.app.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skellig.demo.app.rest.model.BookEventRequest;
import org.skellig.demo.app.rest.model.BookEventResponse;
import org.skellig.demo.app.rest.model.BookingPaymentRequest;
import org.skellig.demo.app.service.BookingService;
import org.skellig.demo.app.service.BookingSubscriptionService;
import org.skellig.demo.app.service.EventService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BookingRequestController {

    private JmsTemplate jmsTemplate;
    private RabbitTemplate rmqTemplate;
    private BookingService bookingService;
    private EventService eventService;
    private ObjectMapper objectMapper;
    private BookingSubscriptionService bookingSubscriptionService;

    @Autowired
    public BookingRequestController(JmsTemplate jmsTemplate,
                                    RabbitTemplate rmqTemplate,
                                    BookingService bookingService,
                                    EventService eventService,
                                    BookingSubscriptionService bookingSubscriptionService) {
        this.jmsTemplate = jmsTemplate;
        this.rmqTemplate = rmqTemplate;
        this.bookingService = bookingService;
        this.eventService = eventService;
        this.bookingSubscriptionService = bookingSubscriptionService;
        objectMapper = new ObjectMapper();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping("/booking/request")
    public Object bookingRequest(@RequestBody BookEventRequest request) throws JsonProcessingException {
        BookEventResponse bookEventResponse = bookingService.bookEvent(request);
        jmsTemplate.convertAndSend("DEV.QUEUE.3", bookEventResponse);

        Message response = rmqTemplate.sendAndReceive("bookings", "booking.log.req", createAmqpMessageFrom(request));
        if (response == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to receive confirmation from Booking Log System");
        }
        return response.getBody();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping("/booking/confirm")
    public void bookingConfirmationRequest(@RequestBody BookingPaymentRequest request) {
        try {
            bookingService.confirmBooking(request.getBookingCode(), request.getPaymentProviderToken());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping("/booking/cancel/{code}")
    public void cancelBookingRequest(@PathVariable long code) {
        try {
            bookingService.cancelBooking(code);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/booking/report/notify/all")
    public void notifyBookingsReport() {
        bookingSubscriptionService.sendReportUpdatesToSubscribers();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Message createAmqpMessageFrom(@RequestBody BookEventRequest request) throws JsonProcessingException {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        return MessageBuilder.withBody(objectMapper.writeValueAsBytes(request)).andProperties(messageProperties).build();
    }
}