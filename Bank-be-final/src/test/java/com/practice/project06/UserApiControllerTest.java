package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.user.User;
import com.practice.project06.user.UserController;
import com.practice.project06.user.UserRepository;
import com.practice.project06.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class UserApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private UserService userService;

    private String jwtToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authenticate();
    }

    private void authenticate() throws Exception {
        var loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"admin\", \"password\":\"admin123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        // Extract the JWT token from the JSON response
        String responseBody = loginResponse.getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        jwtToken = jsonResponse.get("token").asText();
    }

    @Order(1)
    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/all")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Order(2)
    @Test
    public void testGetUserById_Success() throws Exception {
        User user = new User();
        user.setUserID(1L);
        user.setUsername("abc");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("abc"));
    }

    @Test
    public void testLoadUserByUsername_Success() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("admin123");
        User user = new User();
        user.setUsername("admin");
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        Optional<User> userDetails= userRepository.findByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.get().getUsername());
        assertEquals(encodedPassword, userDetails.get().getPassword());
    }

    @Order(5)
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user1")
        );
        assertEquals("User or passowrd incorrect.", thrown.getMessage());
    }

}
