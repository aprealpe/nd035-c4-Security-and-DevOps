package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.Constants;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    private User user = new User();

    @Before
    public void init() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", passwordEncoder);


        user.setId(Constants.ID);
        user.setUsername(Constants.USERNAME);
        user.setPassword(Constants.PASSWORD);

        when(userRepository.findById(Constants.ID)).thenReturn(java.util.Optional.ofNullable(user));
    }

    @Test
    public void findUserById() {
        ResponseEntity<User> userResponse = userController.findById(Constants.ID);
        assertEquals(Constants.ID.intValue(), userResponse.getBody().getId());
        userResponse = userController.findById(Constants.ID2);
        assertEquals(404, userResponse.getStatusCodeValue());
    }

    @Test
    public void createUser() {
        when(passwordEncoder.encode(Constants.PASSWORD)).thenReturn(Constants.HASHEDPASSWORD);
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(Constants.USERNAME);
        createUserRequest.setPassword(Constants.PASSWORD);
        createUserRequest.setConfirmPassword(Constants.PASSWORD);

        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Constants.USERNAME, response.getBody().getUsername());
        assertEquals(Constants.HASHEDPASSWORD, response.getBody().getPassword());
    }

}
