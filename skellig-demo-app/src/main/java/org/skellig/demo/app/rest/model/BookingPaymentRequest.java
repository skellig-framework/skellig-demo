package org.skellig.demo.app.rest.model;

public class BookingPaymentRequest {

    private long bookingCode;
    private String paymentProviderToken;
    private float amount;

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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
