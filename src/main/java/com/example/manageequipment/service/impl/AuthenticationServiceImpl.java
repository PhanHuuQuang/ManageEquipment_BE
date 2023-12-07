package com.example.manageequipment.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.manageequipment.auth.AuthenticationRequest;
import com.example.manageequipment.auth.AuthenticationResponse;
import com.example.manageequipment.auth.RequestRefreshToken;
import com.example.manageequipment.model.Role;
import com.example.manageequipment.model.User;
import com.example.manageequipment.repository.RoleCustomRepo;
import com.example.manageequipment.repository.UserRepository;
import com.example.manageequipment.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RoleCustomRepo roleCustomRepo;

    public final static String secret_key = "123";
    boolean isAdmin;


    @Override
    public AuthenticationResponse adminAuthenticate(AuthenticationRequest authenticationRequest) {
        isAdmin = false;
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword())
        );

        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is incorrect!!"));
        Role role = null;
        if(user != null) {
            role = roleCustomRepo.findRoleByEmail(user.getEmail());
        }

        assert role != null;
        if (role.getName().equals("ADMIN")) {
            isAdmin = true;
        }

        if(isAdmin) {
            System.out.println("authen: " + authenticationRequest.getEmail());
            System.out.println("authen: " + authenticationRequest.getEmail());


            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Role userRole = new Role();

            userRole = role;

//        user.setRoles(set);
            authorities.add(new SimpleGrantedAuthority(userRole.getName()));

            var jwtToken = jwtService.generateToken(user, authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

            return new AuthenticationResponse(jwtToken, jwtRefreshToken, user);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public AuthenticationResponse userAuthenticate(AuthenticationRequest authenticationRequest) {
        isAdmin = false;
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword())
        );

        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is incorrect!!"));
        Role role = null;
        if(user != null) {
            role = roleCustomRepo.findRoleByEmail(user.getEmail());
        }

        assert role != null;
        if (role.getName().equals("ADMIN")) {
            isAdmin = true;
        }

        if(!isAdmin) {
            System.out.println("authen: " + authenticationRequest.getEmail());
            System.out.println("authen: " + authenticationRequest.getEmail());


            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Role userRole = new Role();

            userRole = role;

//        user.setRoles(set);
            authorities.add(new SimpleGrantedAuthority(userRole.getName()));

            var jwtToken = jwtService.generateToken(user, authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

            return new AuthenticationResponse(jwtToken, jwtRefreshToken, user);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public AuthenticationResponse refreshToken(RequestRefreshToken requestRefreshToken) {

        DecodedJWT decodedJWT = JWT.decode(requestRefreshToken.getRefreshToken());
        System.out.println("subject: "+ decodedJWT.getSubject());
        String email = decodedJWT.getSubject();

        User user = userRepository.findByEmail(email).get();

        Role role = null;

        if(user != null) {
            role = roleCustomRepo.findRoleByEmail(user.getEmail());
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Role userRole = new Role();

        userRole = role;

        user.setRole(userRole);

        authorities.add(new SimpleGrantedAuthority(userRole.getName()));

        var newJwtToken = jwtService.generateToken(user, authorities);

        return new AuthenticationResponse(newJwtToken, requestRefreshToken.getRefreshToken(), user);
    }
}
