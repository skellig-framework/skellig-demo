package org.skellig.demo.app.rest.model;

public class BookEventResponse {

    private String eventCode;
    private long bookingCode;
    private String paymentToken;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public long getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(long bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }
}
