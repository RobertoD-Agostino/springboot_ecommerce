package ecommerce.demoecommerce.configuration;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
            //Crezione filtro 
            final String authHeader = request.getHeader("Authorization"); 
            final String jwt;
            final String userEmail;
            //AuthCheck
            if (authHeader==null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request, response);
                return;
            }
            //Estrazione del token dall'authHeader
            jwt = authHeader.substring(7);
            //Dopo il check del token dobbiamo chiamare lo UserDetailsService per vedere se abbiamo già l'utente nel nostro db o no. Per farlo chiamiamo un servizio Jwt per estrarre lo username.
            //Estrazione userEmail dal token tramite una classe che può manipolare i Jwt token ovvero jwtService 
            userEmail = jwtService.
            extractUsername(jwt);
            
            //verifichiamo se l'email esiste e se l'utente non è autenticato
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
              //verifichiamo che l'utente sia nel db
              UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
              //verifichiamo se il token è valido
              if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null , userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //ora aggiorniamo il SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
              }
            }
            filterChain.doFilter(request, response);
    }   
    
}
