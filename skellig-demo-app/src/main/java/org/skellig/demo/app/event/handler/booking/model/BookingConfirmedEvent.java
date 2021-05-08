package org.skellig.demo.app.event.handler.booking.model;

public class BookingConfirmedEvent {

    private long bookingCode;
    private String paymentProviderToken;

    public long getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(long bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getPaymentProviderToken() {
        return paymentProviderToken;
    }

    public void setPaymentProviderToken(String paymentProviderToken) {
        this.paymentProviderToken = paymentProviderToken;
    }
}