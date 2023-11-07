package ecommerce.demoecommerce.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.demoecommerce.auth.AuthenticationRequest;
import ecommerce.demoecommerce.auth.AuthenticationResponse;
import ecommerce.demoecommerce.auth.AuthenticationService;
import ecommerce.demoecommerce.auth.RegisterRequest;
import ecommerce.demoecommerce.configuration.JwtService;
import ecommerce.demoecommerce.entities.Users;
import ecommerce.demoecommerce.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    
    private final AuthenticationService authenticationService;
    private final UsersService usersService;
    private final JwtService jwtService;

    @PostMapping("/register-user") 
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @GetMapping("/findByEmail")
    @PreAuthorize("hasAuthority ('ADMIN')")
    public ResponseEntity findByEmail(@RequestParam ("email") String email){
        try {
            // String email = jwtService.extractEmailFromRequest(emailRequest);
            Users ret = usersService.findByEmail(email);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (RuntimeException e) {
            String error = "Errore : " + e.getClass().getSimpleName();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteUsers")
    @PreAuthorize("hasAuthority ('ADMIN')")
    public ResponseEntity deleteUsers(@RequestParam("email") String email){
        try { 
            usersService.removeUsers(email);
            return new ResponseEntity<>("Utente " + email + " eliminato", HttpStatus.OK);
        } catch (RuntimeException e) {
            String error = "Errore: " + e.getClass().getSimpleName() + " l'utente non esiste o non è registrato";
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority ('ADMIN')")
    public ResponseEntity getAllUsers(@RequestParam("sizePage") int sizePage, @RequestParam("numberPage")int numberPage){
        try {
            Page<Users> ret = usersService.getAllUsers(numberPage,sizePage);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (Exception e) {
            String error = "Errore : " + e.getClass().getSimpleName(); 
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifyAnagraphic")
    @PreAuthorize("hasAuthority ('USER')")
    public ResponseEntity modifyAnagraphics (HttpServletRequest requestEmail, @RequestParam("newName") String newName, @RequestParam("newSurname") String newSurname, @RequestParam("newBudget") int newBudget){
        try {
            String email = jwtService.extractEmailFromRequest(requestEmail);
            Users ret = usersService.modifyAnagraphic(email, newName, newSurname, newBudget);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (Exception e) {
            String error = "Errore : " + e.getClass().getSimpleName(); 
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modifyEmail")
    @PreAuthorize("hasAuthority ('USER')")
    public ResponseEntity modifyEmail(HttpServletRequest requestEmail, @RequestParam("newEmail") String newEmail){
        try {
            String email = jwtService.extractEmailFromRequest(requestEmail);
            Users ret = usersService.modifyEmail(email, newEmail);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (Exception e) {
            String error = "Errore : " + e.getClass().getSimpleName(); 
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/modifyPassword")
    @PreAuthorize("hasAuthority ('USER')")
    public ResponseEntity modifyPassword(HttpServletRequest requestEmail, @RequestParam("newPassword") String newPassword){
        try {
            String email = jwtService.extractEmailFromRequest(requestEmail);
            Users ret = usersService.modifyPassword(email, newPassword);
            return new ResponseEntity<>(ret,HttpStatus.OK);
        } catch (Exception e) {
            String error = "Errore : " + e.getClass().getSimpleName(); 
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    
}
