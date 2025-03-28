package itmo.tg.airbnb_business.business.misc;

import itmo.tg.airbnb_business.auth.model.User;
import itmo.tg.airbnb_business.business.dto.*;
import itmo.tg.airbnb_business.business.model.Advertisement;
import itmo.tg.airbnb_business.business.model.Booking;
import itmo.tg.airbnb_business.business.model.Fine;
import itmo.tg.airbnb_business.business.model.GuestComplaint;

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

    public static FineDTO convert(Fine fine) {
        return FineDTO.builder()
                .id(fine.getId())
                .amount(fine.getAmount())
                .status(fine.getStatus())
                .username(fine.getUser().getUsername())
                .build();
    }

    public static List<FineDTO> toFineDTOList(List<Fine> fines) {
        return fines.stream().map(ModelDTOConverter::convert).toList();
    }

    public static GuestComplaintResponseDTO convert(GuestComplaint complaint) {
        return GuestComplaintResponseDTO.builder()
                .id(complaint.getId())
                .guestUsername(complaint.getGuest().getUsername())
                .advertisementId(complaint.getAdvertisement().getId())
                .bookingId(complaint.getBooking().getId())
                .proofLink(complaint.getProofLink())
                .date(complaint.getDate())
                .status(complaint.getStatus())
                .resolverUsername(complaint.getResolver() == null ? null : complaint.getResolver().getUsername())
                .build();
    }

    public static List<GuestComplaintResponseDTO> toGuestComplaintDTOList(List<GuestComplaint> complaints) {
        return complaints.stream().map(ModelDTOConverter::convert).toList();
    }

}
