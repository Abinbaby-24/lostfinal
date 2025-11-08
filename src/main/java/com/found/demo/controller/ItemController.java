package com.found.demo.controller;

import com.found.demo.model.Item;
import com.found.demo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/items")
public class ItemController {
    
    @Autowired
    private ItemService itemService;
    
    @GetMapping
    @ResponseBody
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
    
    @GetMapping("/status/{status}")
    @ResponseBody
    public List<Item> getItemsByStatus(@PathVariable String status) {
        return itemService.getItemsByStatus(status);
    }
    
    @PostMapping
    @ResponseBody
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.ok(savedItem);
    }
    
    // SEARCH ENDPOINT
    @GetMapping("/search")
    @ResponseBody
    public List<Item> searchItems(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date) {
        
        return itemService.searchItems(query, type, status, date);
    }
}