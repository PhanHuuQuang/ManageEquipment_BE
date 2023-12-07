package com.example.manageequipment.service;

import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.model.Request;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RequestService {

    RequestDto createRequestEquipment(RequestDto requestDto);

    List<RequestDto> getRequestEquipmentByUserId(Long userId);

    List<RequestDto> getAllRequestEquipment();

    String rejectRequestEquipment(Long requestId);

    String confirmRequestEquipment(Long requestId);

    void deleteRequest(List<Long> ids);
}
