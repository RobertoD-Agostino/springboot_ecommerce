package ecommerce.demoecommerce.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.demoecommerce.entities.Product;
import ecommerce.demoecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;

    @PostMapping("/addProduct")
    @PreAuthorize("hasAuthority ('ADMIN')")
    public ResponseEntity addProduct(@RequestBody Product p){
        try {
            Product ret = productService.addProduct(p);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (RuntimeException e) {
            String error = "Errore: " + getClass().getSimpleName();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
