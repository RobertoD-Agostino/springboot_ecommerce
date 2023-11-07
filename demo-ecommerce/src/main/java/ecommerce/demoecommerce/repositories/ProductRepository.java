package ecommerce.demoecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.demoecommerce.entities.Product;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Integer>{
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
}
