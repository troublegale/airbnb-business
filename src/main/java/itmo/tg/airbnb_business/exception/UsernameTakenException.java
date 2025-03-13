package itmo.tg.airbnb_business.exception;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException(String message) {
        super(message);
    }

}
