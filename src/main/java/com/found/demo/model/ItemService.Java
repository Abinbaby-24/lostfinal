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
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByStatus(String status) {
        ItemStatus itemStatus = ItemStatus.valueOf(status.toUpperCase());
        return itemRepository.findByStatus(itemStatus);
    }

    // Returns Item directly (null if not found) — used by controller
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public Item saveItem(Item item) {
        if (item.getReportedDate() == null) {
            item.setReportedDate(LocalDate.now());
        }
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> searchItems(String query, String type, String status, String date) {
        ItemStatus itemStatus = (status != null && !status.isEmpty())
                ? ItemStatus.valueOf(status.toUpperCase()) : null;
        LocalDate reportedDate = (date != null && !date.isEmpty())
                ? LocalDate.parse(date) : null;

        List<Item> results;

        if ((query == null || query.isEmpty()) && reportedDate != null) {
            if (itemStatus != null) {
                results = itemRepository.findByReportedDateAndStatus(reportedDate, itemStatus);
            } else {
                results = itemRepository.findByReportedDate(reportedDate);
            }
            return results;
        }

        if (query == null || query.isEmpty()) {
            return itemStatus != null
                    ? itemRepository.findByStatus(itemStatus)
                    : itemRepository.findAll();
        }

        if (itemStatus != null) {
            switch (type != null ? type : "all") {
                case "itemName":    results = itemRepository.searchByItemNameAndStatus(query, itemStatus); break;
                case "location":    results = itemRepository.searchByLocationAndStatus(query, itemStatus); break;
                case "description": results = itemRepository.searchByDescriptionAndStatus(query, itemStatus); break;
                default:            results = itemRepository.searchAllFieldsWithStatus(query, itemStatus);
            }
        } else {
            switch (type != null ? type : "all") {
                case "itemName":    results = itemRepository.findByItemNameContainingIgnoreCase(query); break;
                case "location":    results = itemRepository.findByLocationContainingIgnoreCase(query); break;
                case "description": results = itemRepository.findByDescriptionContainingIgnoreCase(query); break;
                default:            results = itemRepository.searchAllFields(query);
            }
        }

        if (reportedDate != null) {
            final LocalDate filterDate = reportedDate;
            results = results.stream()
                    .filter(item -> item.getReportedDate().equals(filterDate))
                    .collect(Collectors.toList());
        }

        return results;
    }
}