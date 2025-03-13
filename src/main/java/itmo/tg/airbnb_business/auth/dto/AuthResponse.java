package itmo.tg.airbnb_business.auth.dto;

import itmo.tg.airbnb_business.auth.model.Role;

public record AuthResponse(String username, String token, Role role){
}
