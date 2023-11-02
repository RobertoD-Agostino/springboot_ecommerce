package ecommerce.demoecommerce.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.demoecommerce.configuration.JwtService;
import ecommerce.demoecommerce.entities.Role;
import ecommerce.demoecommerce.entities.Users;
import ecommerce.demoecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest request) {
        Users user = Users.builder()
        .name(request.getFirstname())
        .surname(request.getLastname())
        .email(request.getEmail())
        //prima di salvare la password dobbiamo codificarla, per farlo iniettiamo prima PasswordEncoder
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }


    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        Users user = Users.builder()
        .name(request.getFirstname())
        .surname(request.getLastname())
        .email(request.getEmail())
        //prima di salvare la password dobbiamo codificarla, per farlo iniettiamo prima PasswordEncoder
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ADMIN)
        .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Users user = repository.findByEmail(request.getEmail()).orElseThrow();
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();        
    }
}
