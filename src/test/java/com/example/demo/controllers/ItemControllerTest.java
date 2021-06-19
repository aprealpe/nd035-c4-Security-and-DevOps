package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.utils.Constants;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);



    @Before
    public void init() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);

        List<Item> ListOfItems = new ArrayList<>();
        List<Item> ListOfItemSameName = new ArrayList<>();

        Item item = new Item();
        item.setId(Constants.ID);
        item.setName(Constants.ITEM);
        item.setPrice(BigDecimal.valueOf(Constants.PRICE));
        when(itemRepository.findById(Constants.ID)).thenReturn(Optional.of(item));

        Item item2 = new Item();
        item2.setId(Constants.ID2);
        item2.setName(Constants.ITEM2);
        item2.setPrice(BigDecimal.valueOf(Constants.PRICE2));

        ListOfItemSameName.add(item);
        when(itemRepository.findByName(Constants.ITEM)).thenReturn(ListOfItemSameName);
        ListOfItems.add(item);
        ListOfItems.add(item2);
        when(itemRepository.findAll()).thenReturn(ListOfItems);

    }

    @Test
    public void getItemById() {
        ResponseEntity<Item> response = itemController.getItemById(Constants.ID);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Constants.ITEM, response.getBody().getName());
    }

    @Test
    public void getItemByName() {
        ResponseEntity<List<Item>>  response = itemController.getItemsByName(Constants.ITEM);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void getItems() {
        ResponseEntity<List<Item>>  response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}
