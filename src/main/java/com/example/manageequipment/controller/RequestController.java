package com.example.manageequipment.controller;

import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.publisher.RabbitMQProducer;
import com.example.manageequipment.service.RequestService;
import com.example.manageequipment.type.IntegerArrayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @Autowired
    private RabbitMQProducer producer;

    @PostMapping("/create-request")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<String> createRequestEquipment(@RequestBody RequestDto requestDto) {
        producer.createRequestAndSendNotification(requestDto);
        return ResponseEntity.ok("Create request success!!");
    }

    @GetMapping("/get-request-by-user/{userId}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<RequestDto>> getRequestEquipmentByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(requestService.getRequestEquipmentByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/get-all-request")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<RequestDto>> getAllRequestEquipment() {
        return new ResponseEntity<>(requestService.getAllRequestEquipment(), HttpStatus.OK);
    }

    @PostMapping("/reject-request/{requestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> rejectRequestEquipment(@PathVariable Long requestId) {
        producer.rejectRequestAndSendNotification(requestId);
        return new ResponseEntity<>("Reject request success!!", HttpStatus.OK);
    }

    @PostMapping("/confirm-requestg /{requestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> confirmRequestEquipment(@PathVariable Long requestId) {
        producer.confirmRequestAndSendNotification(requestId);
        return new ResponseEntity<>("Confirm request success!!", HttpStatus.OK);
    }

    @DeleteMapping("/delete-request")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> deleteRequestEquipment(@RequestBody IntegerArrayRequest requestIds) {
        requestService.deleteRequest(requestIds.getIds());
        return new ResponseEntity<>("Delete request success!!", HttpStatus.OK);
    }

}
