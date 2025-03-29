package itmo.tg.airbnb_business.business.exception.exceptions;

public class TicketAlreadyResolvedException extends RuntimeException {
    public TicketAlreadyResolvedException(String message) {
        super(message);
    }
}
