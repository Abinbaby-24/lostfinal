package com.found.demo.service;

import com.found.demo.model.Item;
import com.found.demo.model.ItemStatus;
import com.found.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemSearchService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    /**
     * Main search method with multiple filters
     */
    public List<Item> searchItems(String query, String type, String status, String date) {
        ItemStatus itemStatus = (status != null && !status.isEmpty()) ? ItemStatus.valueOf(status) : null;
        LocalDate reportedDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        
        List<Item> results;
        
        // If only date filter
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
            if (itemStatus != null) {
                return itemRepository.findByStatus(itemStatus);
            } else {
                return itemRepository.findAll();
            }
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
}