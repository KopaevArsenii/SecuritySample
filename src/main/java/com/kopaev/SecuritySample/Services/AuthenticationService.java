package com.kopaev.SecuritySample.Services;

import com.kopaev.SecuritySample.DTO.Response.SuccessResponse;
import com.kopaev.SecuritySample.DTO.Request.LoginRequest;
import com.kopaev.SecuritySample.DTO.Request.RegisterRequest;
import com.kopaev.SecuritySample.models.Role;
import com.kopaev.SecuritySample.models.User;
import com.kopaev.SecuritySample.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public SuccessResponse.AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is taken");
        }

        var user = User.builder()
                .username(request.getUsername())
                .realName(request.getRealName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return SuccessResponse.AuthenticationResponse.builder().token(jwtToken).build();
    }
    public SuccessResponse.AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return SuccessResponse.AuthenticationResponse.builder().token(jwtToken).build();
    }

    public boolean checkToken(String token){
        return jwtService.isTokenValid(token);
    }
}
