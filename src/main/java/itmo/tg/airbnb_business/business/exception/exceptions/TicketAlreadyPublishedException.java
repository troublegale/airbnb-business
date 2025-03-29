package itmo.tg.airbnb_business.business.exception.exceptions;

public class TicketAlreadyPublishedException extends RuntimeException {
    public TicketAlreadyPublishedException(String message) {
        super(message);
    }
}
