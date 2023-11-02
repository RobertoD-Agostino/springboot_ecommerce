package ecommerce.demoecommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ecommerce.demoecommerce.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    
    private final UsersRepository repository;
    
    //questa annotazione Bean indica a spring che questo metodo rappresenta un contenitore
    @Bean
    public UserDetailsService userDetailsService(){
        //ritorniamo lo username che abbiamo in input nel metodo loadUserByUserName
        return username -> repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));


        //ritorniamo un UserDetailsService e implementiamo il caricamento dell'utente per nome utente
        // return new UserDetailsService() {
        //     @Override
        //     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        //         return null;
        //     }
        // };
    }

    @Bean
    //L'AuthenticationProvider è sia l'oggetto di accesso ai dati che è responsabile di recuperare i dettagli dell'utente (UserDetails) e anche di codificare la password ecc.
    public AuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //diciamo al fornitore di autenticazione quale servizio di UserDetails utilizzare per recuperare informazioni sul nostro utente perchè i dati possono essere dal database, dal db in memoria ecc.
    authProvider.setUserDetailsService(userDetailsService());
    //ora dobbiamo fornire una password sul codificatore, quindi la password che stiamo utilizzando all'interno dell'applicazione per utilizzare il corretto algoritmo di decodifica
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;

    }

    //Ora dobbiamo implentare l'authenticationManager ovvero colui che gestisce l'autenticazione
    @Bean
    public AuthenticationManager authenticationManager
    //config contiene già le informazioni sull'AuthenticationManager
    (AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
