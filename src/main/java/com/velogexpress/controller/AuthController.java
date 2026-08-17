package com.velogexpress.controller;

import com.velogexpress.securite.JwtUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
//@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        //System.out.println("LOGIN REQUEST => " + request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(request.getUsername());

            String token = jwtUtil.generateToken(userDetails);

            System.out.println("TOKEN GENERATED");

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            System.out.println("🔥 AUTH ERROR 🔥");
            e.printStackTrace();   // ⬅️ OBLIGATOIRE
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "error", e.getClass().getSimpleName(),
                            "message", e.getMessage()
                    ));
        }
    }
}


@Data
class AuthRequest {
    private String username;
    private String password;
}

