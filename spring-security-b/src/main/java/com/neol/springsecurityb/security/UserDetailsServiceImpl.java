package com.neol.springsecurityb.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = getById(username);
        if (usuario == null){
            throw new UsernameNotFoundException(username);
        }
        return User.withUsername(username).password(usuario.password()).roles(usuario.roles().toArray(new String[0])).build();
    }

    public record Usuario(String username, String password, Set<String> roles ){}

    public Usuario getById(String username){
        var password = "$2a$12$J0qxanpRxCUrfvN1/tgTlu71uNuyFscHbvWVRzdy3L0CaPyIdBneO";
        Usuario enol = new Usuario("enol", password, Set.of("USER"));
        Usuario maria = new Usuario("maria", password, Set.of("ADMIN"));
        var usuarios = List.of(enol, maria);
        return usuarios.stream().filter(e -> e.username().equalsIgnoreCase(username)).findFirst().orElse(null);
    }
}
