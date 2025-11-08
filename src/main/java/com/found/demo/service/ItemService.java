package com.found.demo.service;

import com.found.demo.model.Item;
import java.util.List;

public interface ItemService {
    
    Item saveItem(Item item);
    
    List<Item> getAllItems();
    
    List<Item> getItemsByStatus(String status);
    
    Item getItemById(Long id);
    
    void deleteItem(Long id);
    
    long countLostItems();
    
    long countFoundItems();
    
    // New search method
    List<Item> searchItems(String query, String type, String status, String date);
}