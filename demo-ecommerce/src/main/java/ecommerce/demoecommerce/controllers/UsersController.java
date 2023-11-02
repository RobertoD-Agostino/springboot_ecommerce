package ecommerce.demoecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.demoecommerce.auth.AuthenticationRequest;
import ecommerce.demoecommerce.auth.AuthenticationResponse;
import ecommerce.demoecommerce.auth.AuthenticationService;
import ecommerce.demoecommerce.auth.RegisterRequest;
import ecommerce.demoecommerce.entities.Users;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }



}
