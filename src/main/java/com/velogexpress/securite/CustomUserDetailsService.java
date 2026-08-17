package com.velogexpress.securite;

import com.velogexpress.entity.Clientregister;
import com.velogexpress.repository.ClientRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // ✅ Important : indique à Spring de gérer cette classe
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRegisterRepository clientRegisterRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Clientregister user = clientRegisterRepository.authenticateUser(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String password = user.getPassword();

        // 👇 SI PA GEN PREFIX → BASE64
        if (!password.startsWith("{")) {
            password = "{base64}" + password;
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());


        return User.builder()
                .username(username) // ✅ OBLIGATOIRE
                // ou user.getEmail() si login = email
                .password(password)
                .authorities(authority)
                .build();
    }

}

