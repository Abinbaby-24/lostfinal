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

    // GET all items
    @GetMapping
    @ResponseBody
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    // GET items by status
    @GetMapping("/status/{status}")
    @ResponseBody
    public List<Item> getItemsByStatus(@PathVariable String status) {
        return itemService.getItemsByStatus(status);
    }

    // GET single item by id
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        if (item == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    // POST create new item
    @PostMapping
    @ResponseBody
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item savedItem = itemService.saveItem(item);
        return ResponseEntity.ok(savedItem);
    }

    // PUT update existing item
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        Item existing = itemService.getItemById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setItemName(item.getItemName());
        existing.setDescription(item.getDescription());
        existing.setCategory(item.getCategory());
        existing.setLocation(item.getLocation());
        existing.setStatus(item.getStatus());
        existing.setContactName(item.getContactName());
        existing.setContactEmail(item.getContactEmail());
        existing.setContactPhone(item.getContactPhone());

        Item updated = itemService.saveItem(existing);
        return ResponseEntity.ok(updated);
    }

    // DELETE item by id
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Item existing = itemService.getItemById(id);
        if (existing == null) return ResponseEntity.notFound().build();
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // SEARCH endpoint
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