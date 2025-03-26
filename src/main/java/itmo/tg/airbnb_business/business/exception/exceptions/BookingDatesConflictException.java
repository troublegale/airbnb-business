package itmo.tg.airbnb_business.business.exception.exceptions;

public class BookingDatesConflictException extends RuntimeException {
    public BookingDatesConflictException(String message) {
        super(message);
    }
}
