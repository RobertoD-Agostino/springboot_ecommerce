package ecommerce.demoecommerce.services;

import org.springframework.stereotype.Service;

import ecommerce.demoecommerce.entities.Product;
import ecommerce.demoecommerce.exceptions.ProductAlreadyExistsException;
import ecommerce.demoecommerce.exceptions.ProductDoesNotExistsException;
import ecommerce.demoecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;

    public Product addProduct(Product p)throws RuntimeException{
        if (productRepository.existsByCode(p.getCode())) {
            throw new ProductAlreadyExistsException(" Ciao prova");
        }else{
            productRepository.save(p);
        }
        return p;
    }   
}
