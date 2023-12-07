package com.example.manageequipment.controller;

import com.example.manageequipment.dto.UserDto;
import com.example.manageequipment.model.User;
import com.example.manageequipment.service.UserService;
import com.example.manageequipment.type.IntegerArrayRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create/{role}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody User user, @PathVariable String role) {
        return new ResponseEntity<>(userService.createUser(user, role) , HttpStatus.CREATED);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/update/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
        return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestBody IntegerArrayRequest userIds) {
        userService.deleteUser(userIds.getIds());
        return new ResponseEntity<>("Delete user success!!", HttpStatus.OK);
    }

    @PostMapping("/create-device-token/{userId}/{deviceToken}")
    @PreAuthorize("hasAnyAuthority('ADMIN') or #userId == authentication.credentials")
    public ResponseEntity<String> createDeviceToken(@PathVariable String deviceToken, @PathVariable Long userId) {
        userService.createDeviceToken(deviceToken, userId);
        return new ResponseEntity<>("Create device token success!!", HttpStatus.OK);
    }
}
