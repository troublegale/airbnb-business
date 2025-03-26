package itmo.tg.airbnb_business.business.exception.exceptions;

public class ActiveBookingsException extends RuntimeException {
    public ActiveBookingsException(String message) {
        super(message);
    }
}
