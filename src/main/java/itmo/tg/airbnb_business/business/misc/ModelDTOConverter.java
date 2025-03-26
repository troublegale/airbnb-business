package itmo.tg.airbnb_business.business.misc;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.AdvertisementRequestDTO;
import itmo.tg.airbnb_business.business.dto.AdvertisementResponseDTO;
import itmo.tg.airbnb_business.business.dto.BookingRequestDTO;
import itmo.tg.airbnb_business.business.dto.BookingResponseDTO;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.Booking;

import java.util.List;

public class ModelDTOConverter {

    public static Advertisement convert(AdvertisementRequestDTO dto, User host) {
        return Advertisement.builder()
                .address(dto.getAddress())
                .rooms(dto.getRooms())
                .bookPrice(dto.getBookPrice())
                .pricePerNight(dto.getPricePerNight())
                .host(host)
                .build();
    }

    public static AdvertisementResponseDTO convert(Advertisement advertisement) {
        return AdvertisementResponseDTO.builder()
                .id(advertisement.getId())
                .address(advertisement.getAddress())
                .rooms(advertisement.getRooms())
                .bookPrice(advertisement.getBookPrice())
                .pricePerNight(advertisement.getPricePerNight())
                .status(advertisement.getStatus())
                .hostUsername(advertisement.getHost().getUsername())
                .build();
    }

    public static List<AdvertisementResponseDTO> toAdvertisementDTOList(List<Advertisement> adverts) {
        return adverts.stream().map(ModelDTOConverter::convert).toList();
    }

    public static Booking convert(BookingRequestDTO dto, Advertisement advertisement, User guest) {
        return Booking.builder()
                .advertisement(advertisement)
                .guest(guest)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }

    public static BookingResponseDTO convert(Booking booking) {
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .advertisementId(booking.getAdvertisement().getId())
                .guestUsername(booking.getGuest().getUsername())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingResponseDTO> toBookingDTOList(List<Booking> bookings) {
        return bookings.stream().map(ModelDTOConverter::convert).toList();
    }

}
