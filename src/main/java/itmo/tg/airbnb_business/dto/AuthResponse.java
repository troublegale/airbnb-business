package itmo.tg.airbnb_business.dto;

import itmo.tg.airbnb_business.model.Role;

import java.io.Serializable;

public record AuthResponse(String username, String token, Role role){
}
