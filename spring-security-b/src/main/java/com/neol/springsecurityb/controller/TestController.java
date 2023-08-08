package com.neol.springsecurityb.controller;

import com.neol.springsecurityb.security.AuthenticationReq;
import com.neol.springsecurityb.security.JwtUtilService;
import com.neol.springsecurityb.security.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api")
public class TestController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @GetMapping
    public ResponseEntity<String>  main(){
        return ResponseEntity.ok("main");
    }



    @GetMapping("/public")
    public ResponseEntity<?> getMensajePublic(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("User details: " + auth.getPrincipal());
        log.info("Rol details: " + auth.getAuthorities());
        log.info("Is authenticated: " + auth.isAuthenticated());
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", "Soy normal");
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getMensajeAdmin(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("User details: " + auth.getPrincipal());
        log.info("Rol details: " + auth.getAuthorities());
        log.info("Is authenticated: " + auth.isAuthenticated());
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", "Soy el admin");
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/public/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq){
        log.info("Autenticando al usuario {}", authenticationReq.getUsuario());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationReq.getUsuario(), authenticationReq.getClave())
        );
        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(authenticationReq.getUsuario());

        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));

    }

}
