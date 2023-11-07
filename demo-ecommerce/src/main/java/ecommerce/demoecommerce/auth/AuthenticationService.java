package ecommerce.demoecommerce.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecommerce.demoecommerce.configuration.JwtService;
import ecommerce.demoecommerce.entities.Role;
import ecommerce.demoecommerce.entities.Users;
import ecommerce.demoecommerce.exceptions.AnagraphicIsNotValidException;
import ecommerce.demoecommerce.exceptions.EmailiSNotValidException;
import ecommerce.demoecommerce.exceptions.PasswordIsNotValidException;
import ecommerce.demoecommerce.repositories.UsersRepository;
import ecommerce.demoecommerce.services.UsersService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    
    private final UsersRepository repository;
    private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest request)throws RuntimeException{

        if (!usersService.emailIsValid(request.getEmail())){
            System.out.println("email");
            throw new EmailiSNotValidException();
        }

        if (!usersService.passwordIsValid(request.getPassword())) {
            System.out.println("password");
            throw new PasswordIsNotValidException();
        }

        if (!usersService.AnagraphicIsValid(request.getName(), request.getSurname())) {
            System.out.println("anagraphic");
            throw new AnagraphicIsNotValidException();
        }

        Users u = Users.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .email(request.getEmail())
        //prima di salvare la password dobbiamo codificarla, per farlo iniettiamo prima PasswordEncoder
        .password(passwordEncoder.encode(request.getPassword()))
        .budget(request.getBudget())
        .role(Role.USER)
        .build();
        repository.save(u);   
        var jwtToken = jwtService.generateToken(u);
        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {

        if (!usersService.emailIsValid(request.getEmail())){
            System.out.println("email");
            throw new EmailiSNotValidException();
        }

        if (!usersService.passwordIsValid(request.getPassword())) {
            System.out.println("password");
            throw new PasswordIsNotValidException();
        }

        if (!usersService.AnagraphicIsValid(request.getName(), request.getSurname())) {
            System.out.println("anagraphic");
            throw new AnagraphicIsNotValidException();
        }
        
        Users user = Users.builder()
        .name(request.getName())
        .surname(request.getSurname())
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
