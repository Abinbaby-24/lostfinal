package com.found.demo.repository;

import com.found.demo.model.Item;
import com.found.demo.model.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // Existing methods
    List<Item> findByStatus(ItemStatus status);
    
    // Count methods - CHANGE FROM String TO ItemStatus
    long countByStatus(ItemStatus status);
    
    // NEW SEARCH METHODS
    List<Item> findByItemNameContainingIgnoreCase(String itemName);
    List<Item> findByLocationContainingIgnoreCase(String location);
    List<Item> findByDescriptionContainingIgnoreCase(String description);
    List<Item> findByReportedDate(LocalDate date);
    
    @Query("SELECT i FROM Item i WHERE " +
           "LOWER(i.itemName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Item> searchAllFields(@Param("query") String query);
    
    @Query("SELECT i FROM Item i WHERE " +
           "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND i.status = :status")
    List<Item> searchAllFieldsWithStatus(@Param("query") String query, @Param("status") ItemStatus status);
    
    @Query("SELECT i FROM Item i WHERE LOWER(i.itemName) LIKE LOWER(CONCAT('%', :query, '%')) AND i.status = :status")
    List<Item> searchByItemNameAndStatus(@Param("query") String query, @Param("status") ItemStatus status);
    
    @Query("SELECT i FROM Item i WHERE LOWER(i.location) LIKE LOWER(CONCAT('%', :query, '%')) AND i.status = :status")
    List<Item> searchByLocationAndStatus(@Param("query") String query, @Param("status") ItemStatus status);
    
    @Query("SELECT i FROM Item i WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%')) AND i.status = :status")
    List<Item> searchByDescriptionAndStatus(@Param("query") String query, @Param("status") ItemStatus status);
    
    List<Item> findByReportedDateAndStatus(LocalDate date, ItemStatus status);
}