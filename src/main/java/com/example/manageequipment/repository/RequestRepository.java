package com.example.manageequipment.repository;

import com.example.manageequipment.model.Request;
import com.example.manageequipment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByUserOwner(User user);
}
