package itmo.tg.airbnb_business.auth.controller;

import itmo.tg.airbnb_business.auth.dto.AuthRequest;
import itmo.tg.airbnb_business.auth.dto.AuthResponse;
import itmo.tg.airbnb_business.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public AuthResponse signUp(@RequestBody AuthRequest authRequest) {
        return authenticationService.signUp(authRequest);
    }

    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody AuthRequest authRequest) {
        return authenticationService.signIn(authRequest);
    }

}
