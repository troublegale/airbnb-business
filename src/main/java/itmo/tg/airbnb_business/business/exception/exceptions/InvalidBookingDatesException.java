package itmo.tg.airbnb_business.business.exception.exceptions;

public class InvalidBookingDatesException extends RuntimeException {
    public InvalidBookingDatesException(String message) {
        super(message);
    }
}
