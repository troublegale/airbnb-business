package itmo.tg.airbnb_business.security.dto;

import itmo.tg.airbnb_business.security.model.Role;

public record AuthResponse(String username, String token, Role role) {
}
