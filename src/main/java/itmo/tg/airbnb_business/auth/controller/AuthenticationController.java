package itmo.tg.airbnb_business.auth.controller;

import itmo.tg.airbnb_business.auth.dto.AuthRequest;
import itmo.tg.airbnb_business.auth.dto.AuthResponse;
import itmo.tg.airbnb_business.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public AuthResponse signUp(@RequestBody @Valid AuthRequest authRequest) {
        return authenticationService.signUp(authRequest);
    }

    @PostMapping("/sign-in")
    public AuthResponse signIn(@RequestBody @Valid AuthRequest authRequest) {
        return authenticationService.signIn(authRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ignored) {
        return new ResponseEntity<>("Bad auth request", HttpStatus.BAD_REQUEST);
    }

}
