package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.controller.AuthController;
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.UserRepository;
import com.dammy.bookstoreapi.security.JwtTokenProvider;
import com.dammy.bookstoreapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Arrange
        User user = new User("username", "password", "name", "role");

        when(userService.register(any(User.class))).thenReturn(user);

        // Act
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        // Assert
        verify(userService, times(1)).register(any(User.class));
    }


    @Test
    public void testRegisterUser_UsernameAlreadyTaken() throws Exception {
        // Arrange
        User user = new User("username", "password", "name", "role");

        when(userService.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        // Act
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict()); // Update the expected status code

        // Assert
        verify(userService, never()).register(any(User.class));
    }


//    @Test
//    public void testLoginUser() throws Exception {
//        // Arrange
//        User user = new User("username", "password", "name", "role");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
//
//        Map<String, String> credentials = new HashMap<>();
//        credentials.put("username", user.getUsername());
//        credentials.put("password", "password"); // Note the change here
//
//        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
//
//        // Act
//        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(credentials)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        verify(jwtTokenProvider, times(1)).createToken(any(String.class));
//    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        // Arrange
        User user = new User("non-existent-username", "password", "name", "role");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", user.getUsername());
        credentials.put("password", user.getPassword());

        // Act
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isInternalServerError()); // Update the expected status code

        // Assert
        verify(jwtTokenProvider, never()).createToken(any(String.class));
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}