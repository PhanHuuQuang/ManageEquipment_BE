package com.example.manageequipment.repository;

//import com.example.manageequipment.model.Category;
import com.example.manageequipment.model.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    List<Equipment> findByNameContaining(String name);

    List<Equipment> findByOwnerId(int ownerId);

//    @Query(value = "SELECT * FROM equipment e WHERE owner_id = :ownerId",
//            countQuery = "SELECT count(*) FROM equipment e WHERE owner_id = :ownerId",
//            nativeQuery = true)
//    List<Equipment> findByOwnerId(int ownerId);
}
