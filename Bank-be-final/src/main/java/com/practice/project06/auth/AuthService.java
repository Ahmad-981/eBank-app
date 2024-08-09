package com.practice.project06.auth;

import com.practice.project06.account.AccountRepository;
import com.practice.project06.account.Account;
import com.practice.project06.user.User;
import com.practice.project06.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.practice.project06.utility.JwtUtil;

import java.util.HashMap;
import java.util.Map;


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

    public Map<String, Object> authenticate(AuthRequest authRequest) {
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

        return response;
    }
}