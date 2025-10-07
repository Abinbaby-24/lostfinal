package com.found.demo.service;



import com.found.demo.model.Item;
import com.found.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }
    
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    @Override
    public List<Item> getItemsByStatus(String status) {
        return itemRepository.findByStatus(status);
    }
    
    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
    
    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
    
    @Override
    public long countLostItems() {
        return itemRepository.countByStatus("LOST");
    }
    
    @Override
    public long countFoundItems() {
        return itemRepository.countByStatus("FOUND");
    }
}