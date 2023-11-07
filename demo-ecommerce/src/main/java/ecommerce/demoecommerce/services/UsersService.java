package ecommerce.demoecommerce.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ecommerce.demoecommerce.auth.RegisterRequest;
import ecommerce.demoecommerce.entities.Users;
import ecommerce.demoecommerce.exceptions.ListIsEmptyException;
import ecommerce.demoecommerce.exceptions.UserDoesNotExistsException;
import ecommerce.demoecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public boolean existsByEmail(String email){
        return usersRepository.existsByEmail(email);
    }

    public Users findByEmail(String email)throws RuntimeException{
        if (usersRepository.existsByEmail(email)) {
            return usersRepository.findByEmail(email).get();
        }else{
            throw new UserDoesNotExistsException();
        }
    }  

    public void removeUsers(String email)throws RuntimeException{
        Users u = findByEmail(email);

        if (u!=null) {
            usersRepository.delete(u);
        }else{
            throw new UserDoesNotExistsException();
        }
    }

    public Page<Users> getAllUsers(int numberPage, int sizePage){
        Pageable pageable = PageRequest.of(numberPage, sizePage);
        return usersRepository.findAll(pageable);
    }

    public boolean AnagraphicIsValid(String name, String surname){
        
        String regexName = "^[A-Za-z]{4,}";
        String regexSurname = "^[A-Za-z]{4,}";  
        if(!name.matches(regexName) || !surname.matches(regexSurname)){
            return false;       
        }
        return true;
    }

    public boolean passwordIsValid(String password){
        String regexPassword = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$";
        if (!password.matches(regexPassword)) {
            return false;
        }

        return true;
    }

    public boolean emailIsValid(String email){
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(regexEmail)) {
            return false;
        }

        return true;
    }

    public Users modifyAnagraphic(String email, String newName, String newSurname, int newBudget){
        Users u = usersRepository.findByEmail(email).orElseThrow(()-> new UserDoesNotExistsException());
        if (AnagraphicIsValid(u.getName(), u.getSurname())) {
            u.setName(newName);
            u.setSurname(newSurname);
            u.setBudget(newBudget);
        }  
        return u;
    }

    public Users modifyEmail(String email, String newEmail){
        Users u = usersRepository.findByEmail(email).orElseThrow(()->new UserDoesNotExistsException());
        if(emailIsValid(u.getEmail()) && !email.equals(newEmail)){
            u.setEmail(newEmail);
        }
        return u;
    }

    public Users modifyPassword(String email,String newPassword){
        Users u = usersRepository.findByEmail(email).orElseThrow(()->new UserDoesNotExistsException());
        String password = u.getPassword();

        if (passwordIsValid(password) && !password.equals(newPassword)){
            u.setPassword(newPassword);
        }
        return u;
    }
}
