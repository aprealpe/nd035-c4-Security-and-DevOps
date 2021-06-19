package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.Constants;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    private Cart cart = new Cart();
    private User user = new User();
    private Item item = new Item();


    @Before
    public void init() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);

        user.setId(Constants.ID);
        user.setUsername(Constants.USERNAME);
        user.setPassword(Constants.PASSWORD);
        when(userRepository.findByUsername(Constants.USERNAME)).thenReturn(user);

        List<Item> ListOfItems = new ArrayList<>();
        item.setId(Constants.ID);
        item.setName(Constants.ITEM);
        item.setPrice(BigDecimal.valueOf(Constants.PRICE));
        item.setDescription(Constants.DESCRIPTION);
        ListOfItems.add(item);

        cart.setId(Constants.ID);
        cart.setItems(ListOfItems);
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(Constants.PRICE));
        user.setCart(cart);
    }

    @Test
    public void submit() {
        ResponseEntity<UserOrder> response = orderController.submit(Constants.USERNAME);
        assertEquals(BigDecimal.valueOf(Constants.PRICE), response.getBody().getTotal());
    }

    @Test
    public void history() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(Constants.USERNAME);
        assertEquals(0, response.getBody().size());
    }
}
