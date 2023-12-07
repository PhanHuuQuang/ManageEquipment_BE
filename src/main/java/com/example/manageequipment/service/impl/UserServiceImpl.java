package com.example.manageequipment.service.impl;

import com.example.manageequipment.dto.UserDto;
import com.example.manageequipment.model.Equipment;
import com.example.manageequipment.model.Role;
import com.example.manageequipment.model.User;
import com.example.manageequipment.repository.EquipmentRepository;
import com.example.manageequipment.repository.RoleCustomRepo;
import com.example.manageequipment.repository.UserRepository;
import com.example.manageequipment.repository.impl.UserRepoImpl;
import com.example.manageequipment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleCustomRepo roleRepository;

    @Autowired
    UserRepoImpl userRepo;

    @Autowired
    EquipmentRepository equipmentRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRole().getName());
        userDto.setDeviceToken(user.getDeviceToken());
        return userDto;
    }


    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void addRoleToStudent(String userEmail, String roleName) {
        System.out.println("role 2: " + roleName);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid student email "+ userEmail));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid role "+ roleName));
        System.out.println("role 3: " + role.getId());

        user.setRole(role);
        User newUser = userRepository.save(user);
    }

    @Override
    public void createDeviceToken(String deviceToken, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException((HttpStatus.NOT_FOUND), "Invalid user userId "+ userId));

        user.setDeviceToken(deviceToken);

        userRepository.save(user);
    }


    @Override
    public UserDto createUser(User user, String role) {
        System.out.println("role: " + role);
        System.out.println("user: " + user);
        saveUser(user);
        System.out.println("save user success");
        addRoleToStudent(user.getEmail(), role);
        User createdUser = userRepository.findById(user.getId()).get();
        return mapToDto(createdUser);
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> users = userRepo.getAllUser();

        return users;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        User userData = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id "+ userId));

        if (user.getFirstName() != null) {
            userData.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userData.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            userData.setEmail(user.getEmail());
        }
        if (user.getAddress() != null) {
            userData.setAddress(user.getAddress());
        }

        User userUpdated = userRepository.save(userData);

        return mapToDto(userUpdated);
    }

    @Override
    public void deleteUser(List<Long> userIds) {
        userIds.stream().forEach(id -> {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id "+ id));
            Set<Equipment> equipments = user.getEquipments();
            equipments.forEach(equip -> {
                equip.setOwner(null);
                equipmentRepository.save(equip);
            });

            if (user.getTransferredEquipment() != null) {
                for (Equipment equip : user.getTransferredEquipment()) {
                    equip.getTransferredUser().remove(user);
                }
            }

            userRepository.delete(user);
        });

    }
}
