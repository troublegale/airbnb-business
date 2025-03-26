package itmo.tg.airbnb_business.business.exception.exceptions;

public class AdvertisementBlockedException extends RuntimeException {
    public AdvertisementBlockedException(String message) {
        super(message);
    }
}
