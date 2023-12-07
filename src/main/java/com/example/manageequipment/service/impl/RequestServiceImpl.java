package com.example.manageequipment.service.impl;

import com.example.manageequipment.dto.RequestDto;
import com.example.manageequipment.model.Category;
import com.example.manageequipment.model.Equipment;
import com.example.manageequipment.model.Request;
import com.example.manageequipment.model.User;
import com.example.manageequipment.repository.CategoryRepository;
import com.example.manageequipment.repository.EquipmentRepository;
import com.example.manageequipment.repository.RequestRepository;
import com.example.manageequipment.repository.UserRepository;
import com.example.manageequipment.repository.impl.RequestRepoImpl;
import com.example.manageequipment.service.EquipmentService;
import com.example.manageequipment.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RequestRepoImpl requestRepo;

    public RequestDto mapToDto(Request request) {
        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(request.getUserOwner().getId());
        requestDto.setState(request.getState());
        requestDto.setId(request.getId());
        if (request.getRequestEquipmentType() == null) {
            requestDto.setRequestEquipmentTypeId(null);
        } else {
            requestDto.setRequestEquipmentTypeId(request.getRequestEquipmentType().getId());
        }
        requestDto.setDescription(request.getDescription());

        return requestDto;
    }

    @Override
    public RequestDto createRequestEquipment(RequestDto requestDto) {
        Request request = new Request();

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id "));

        Category category = categoryRepository.findById(requestDto.getRequestEquipmentTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid category id "));

        request.setDescription(requestDto.getDescription());
        request.setUserOwner(user);
        request.setState("PENDING");
        request.setRequestEquipmentType(category);

        requestRepository.save(request);

        return mapToDto(request);
    }

    @Override
    public List<RequestDto> getRequestEquipmentByUserId(Long userId) {
        List<RequestDto> listRequest = requestRepo.getRequestByUserId(userId);

        return listRequest;
    }

    @Override
    public List<RequestDto> getAllRequestEquipment() {
        List<RequestDto> listRequest = requestRepo.getAllRequest();

        return listRequest;
    }

    @Override
    public String rejectRequestEquipment(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid request id "+ requestId));

        request.setState("REJECTED");

        requestRepository.save(request);

        return "Reject equipment success!!";
    }

    @Override
    public String confirmRequestEquipment(Long requestId) {


        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid request id "+ requestId));

        request.setState("CONFIRMED");

        requestRepository.save(request);

        return "Confirm Request Success!!";
    }

    @Override
    public void deleteRequest(List<Long> ids) {
        ids.forEach(requestId -> {
            Request request = requestRepository.findById(requestId).get();

            if(request.getUserOwner() != null) {
                User owner = request.getUserOwner();
                owner.getRequests().remove(request);
                userRepository.save(owner);
            }

            if(request.getRequestEquipmentType() != null) {
                Category category = request.getRequestEquipmentType();
                category.getRequests().remove(request);
                categoryRepository.save(category);
            }

            requestRepository.deleteById(requestId);
        });
    }
}
