package org.skellig.demo.app.rest;

import org.skellig.demo.app.rest.model.Booking;
import org.skellig.demo.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class BookingReadController {

    private BookingService bookingService;

    @Autowired
    public BookingReadController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String index() {
        return "started";
    }

    @GetMapping("/booking/list")
    @ResponseBody
    public Collection<Booking> bookings() {
        return bookingService.getAll();
    }

    @GetMapping("/booking/{id}")
    @ResponseBody
    public Booking booking(@PathVariable("id") int id) {
        return bookingService.getByBookingCode(id);
    }
}