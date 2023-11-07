package ecommerce.demoecommerce.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.demoecommerce.entities.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
