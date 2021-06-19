package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.utils.Constants;
import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.awt.peer.CanvasPeer;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    private Cart cart = new Cart();
    private User user = new User();
    private Item item = new Item();

    @Before
    public void init() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);

        user.setId(Constants.ID);
        user.setUsername(Constants.USERNAME);
        user.setPassword(Constants.PASSWORD);
        user.setCart(cart);

        item.setId(Constants.ID);
        item.setName(Constants.ITEM);
        item.setPrice(BigDecimal.valueOf(Constants.PRICE));

        when(userRepository.findByUsername(Constants.USERNAME)).thenReturn(user);
        when(itemRepository.findById(Constants.ID)).thenReturn(java.util.Optional.ofNullable(item));
    }

    @Test
    public void addToCart() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(Constants.ID);
        modifyCartRequest.setQuantity(Constants.QUANTITY);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(BigDecimal.valueOf(Constants.PRICE * Constants.QUANTITY), response.getBody().getTotal());
    }

    @Test
    public void removeFromCartOneItem() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setItemId(Constants.ID);
        modifyCartRequest.setQuantity(Constants.QUANTITY);
        modifyCartRequest.setUsername(user.getUsername());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        ModifyCartRequest newCartRequest = new ModifyCartRequest();

        newCartRequest.setItemId(Constants.ID);
        newCartRequest.setQuantity(Constants.QUANTITY - 1);
        newCartRequest.setUsername(user.getUsername());

        response = cartController.removeFromcart(newCartRequest);
        assertEquals(BigDecimal.valueOf(Constants.PRICE * (Constants.QUANTITY - 1)), response.getBody().getTotal());

    }

}
