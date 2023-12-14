package com.example.manageequipment.service;

import com.example.manageequipment.dto.EquipmentDto;
import com.example.manageequipment.dto.UserDto;
import com.example.manageequipment.model.Equipment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface EquipmentService {


    EquipmentDto createEquipment(EquipmentDto equipment) throws IOException;

    List<EquipmentDto> getAllEquipment(String name);

    EquipmentDto updateEquipment(Long equipmentId, EquipmentDto equipmentDto) throws IOException;

    void deleteEquipment(List<Long> ids);

    UserDto transferEquipment(List<Long> equipmentIds, Long userId);

    List<EquipmentDto> getEquipmentByOwnerId(int ownerId);

    UserDto requestEquipment(List<Long> ids, Long userId);

//    List<EquipmentDto> getEquipmentByCategoryId(Long categoryId);
}
