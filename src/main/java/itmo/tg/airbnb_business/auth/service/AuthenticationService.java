package itmo.tg.airbnb_business.auth.service;

import itmo.tg.airbnb_business.auth.dto.AuthRequest;
import itmo.tg.airbnb_business.auth.dto.AuthResponse;
import itmo.tg.airbnb_business.auth.model.Role;
import itmo.tg.airbnb_business.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signUp(AuthRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();
        String token = jwtService.generateToken(user);
        return new AuthResponse(user.getUsername(), token, Role.ROLE_USER);
    }

    public AuthResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), request.password()
        ));
        UserDetails userDetails = userService.getUserDetailsService().loadUserByUsername(request.username());
        User user = (User) userDetails;
        Role role = user.getRole();
        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(user.getUsername(), token, role);
    }

}