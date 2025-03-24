package itmo.tg.airbnb_business.business.exception.exceptions;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException(String message) {
        super(message);
    }
}
