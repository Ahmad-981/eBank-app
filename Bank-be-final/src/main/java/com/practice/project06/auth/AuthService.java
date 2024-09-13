package com.practice.project06.auth;

import com.practice.project06.account.AccountRepository;
import com.practice.project06.account.Account;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.practice.project06.utility.JwtUtil;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

//    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String token = jwtUtil.generateToken(authRequest.getUsername());
//
//        User user = userRepository.findByUsername(authRequest.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Long userId = user.getUserID();
//        String username = user.getUsername();
//        Long accountId = accountRepository.findByUser_UserID(userId).map(Account::getAccountID).orElse(null);
//        String role = user.getRole();
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", token);
//        response.put("userId", userId);
//        response.put("username", username);
//        response.put("accountId", accountId);
//        response.put("role", role);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//
//        return ResponseEntity.ok().headers(headers).body(response);
//    }

    public ResponseEntity<?> authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtil.generateToken(authRequest.getUsername());

        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getUserID();
        String username = user.getUsername();

        Long accountId = accountRepository.findByUser_UserID(userId).map(Account::getAccountID).orElse(null);

        String role = user.getRole();
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", userId);
        response.put("username", username);
        response.put("accountId", accountId);
        response.put("role", role);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        return ResponseEntity.ok().headers(headers).body(response);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public ResponseEntity<?> createUser(@RequestBody User user) {

        String email = user.getEmail();
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User is already registered");
        }
        String address = user.getAddress();
        if(address.length() > 30){
            throw new IllegalArgumentException("Address length should be less that 30 characters");
        }
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}