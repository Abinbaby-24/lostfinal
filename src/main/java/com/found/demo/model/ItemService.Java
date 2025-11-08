package com.lostandfound.service;

import com.lostandfound.model.Item;
import com.lostandfound.model.ItemStatus;
import com.lostandfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    /**
     * Get all items from the database
     */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    /**
     * Get items by status (LOST or FOUND)
     */
    public List<Item> getItemsByStatus(ItemStatus status) {
        return itemRepository.findByStatus(status);
    }
    
    /**
     * Get item by ID
     */
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }
    
    /**
     * Save a new item or update existing item
     */
    public Item saveItem(Item item) {
        if (item.getReportedDate() == null) {
            item.setReportedDate(LocalDate.now());
        }
        return itemRepository.save(item);
    }
    
    /**
     * Delete an item by ID
     */
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    
    /**
     * Search items with multiple filters
     * @param query - search text
     * @param type - field to search in (all, itemName, location, description)
     * @param status - item status (LOST, FOUND, or null for all)
     * @param date - reported date filter
     */
    public List<Item> searchItems(String query, String type, String status, String date) {
        ItemStatus itemStatus = (status != null && !status.isEmpty()) ? ItemStatus.valueOf(status) : null;
        LocalDate reportedDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        
        List<Item> results;
        
        // If only date filter is provided
        if ((query == null || query.isEmpty()) && reportedDate != null) {
            if (itemStatus != null) {
                results = itemRepository.findByReportedDateAndStatus(reportedDate, itemStatus);
            } else {
                results = itemRepository.findByReportedDate(reportedDate);
            }
            return results;
        }
        
        // If no query, return filtered or all items
        if (query == null || query.isEmpty()) {
            return itemStatus != null ? getItemsByStatus(itemStatus) : getAllItems();
        }
        
        // Search based on type
        if (itemStatus != null) {
            // Search with status filter
            switch (type != null ? type : "all") {
                case "itemName":
                    results = itemRepository.searchByItemNameAndStatus(query, itemStatus);
                    break;
                case "location":
                    results = itemRepository.searchByLocationAndStatus(query, itemStatus);
                    break;
                case "description":
                    results = itemRepository.searchByDescriptionAndStatus(query, itemStatus);
                    break;
                default:
                    results = itemRepository.searchAllFieldsWithStatus(query, itemStatus);
            }
        } else {
            // Search without status filter
            switch (type != null ? type : "all") {
                case "itemName":
                    results = itemRepository.findByItemNameContainingIgnoreCase(query);
                    break;
                case "location":
                    results = itemRepository.findByLocationContainingIgnoreCase(query);
                    break;
                case "description":
                    results = itemRepository.findByDescriptionContainingIgnoreCase(query);
                    break;
                default:
                    results = itemRepository.searchAllFields(query);
            }
        }
        
        // Apply date filter if provided
        if (reportedDate != null) {
            final LocalDate filterDate = reportedDate;
            results = results.stream()
                    .filter(item -> item.getReportedDate().equals(filterDate))
                    .collect(Collectors.toList());
        }
        
        return results;
    }
    
    /**
     * Search items by item name only
     */
    public List<Item> searchByItemName(String itemName) {
        return itemRepository.findByItemNameContainingIgnoreCase(itemName);
    }
    
    /**
     * Search items by location only
     */
    public List<Item> searchByLocation(String location) {
        return itemRepository.findByLocationContainingIgnoreCase(location);
    }
    
    /**
     * Get items by date
     */
    public List<Item> getItemsByDate(LocalDate date) {
        return itemRepository.findByReportedDate(date);
    }
    
    /**
     * Get count of all items
     */
    public long getItemCount() {
        return itemRepository.count();
    }
    
    /**
     * Get count of lost items
     */
    public long getLostItemCount() {
        return itemRepository.findByStatus(ItemStatus.LOST).size();
    }
    
    /**
     * Get count of found items
     */
    public long getFoundItemCount() {
        return itemRepository.findByStatus(ItemStatus.FOUND).size();
    }
}