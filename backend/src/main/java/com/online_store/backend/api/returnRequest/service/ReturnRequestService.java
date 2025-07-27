package com.online_store.backend.api.returnRequest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.online_store.backend.api.company.entities.Company;
import com.online_store.backend.api.company.utils.CompanyUtilsService;
import com.online_store.backend.api.order.entities.Order;
import com.online_store.backend.api.order.entities.OrderItem;
import com.online_store.backend.api.order.entities.OrderStatus;
import com.online_store.backend.api.order.repository.OrderRepository;
import com.online_store.backend.api.returnRequest.dto.request.ReturnRequestDto;
import com.online_store.backend.api.returnRequest.dto.request.UpdateReturnRequestDto;
import com.online_store.backend.api.returnRequest.entities.ReturnRequest;
import com.online_store.backend.api.returnRequest.repository.ReturnRequestRepository;
import com.online_store.backend.api.returnRequest.utils.mapper.CreateReturnRequestMapper;
import com.online_store.backend.api.returnRequest.utils.mapper.UpdateReturnRequestMapper;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.common.utils.CommonUtilsService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnRequestService {
    private final ReturnRequestRepository returnRequestRepository;
    private final OrderRepository orderRepository;
    private final CommonUtilsService commonUtilsService;
    private final CreateReturnRequestMapper createReturnRequestMapper;
    private final UpdateReturnRequestMapper updateReturnRequestMapper;
    private CompanyUtilsService companyUtilsService;

    public String createReturnRequest(ReturnRequestDto dto) {
        User user = commonUtilsService.getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);
        OrderItem foundOrderItem = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> orderItem.getId().equals(dto.getOrderItem()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found or does not belong to the current user."));

        ReturnRequest returnRequest = createReturnRequestMapper.returnRequestMapper(dto, foundOrderItem, user);

        returnRequestRepository.save(returnRequest);

        return "Return request created succes.";
    }

    public String updateReturnRequestStatus(Long id,
            UpdateReturnRequestDto updateReturnRequestDto) {
        ReturnRequest returnRequest = returnRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return request not found!"));

        Company company = companyUtilsService.getCurrentUserCompany();

        if (!returnRequest.getOrderItem().getProduct().getCompany().equals(company)) {
            throw new Error("You do not have permission to update this return request.");
        }

        ReturnRequest updatedReturnRequest = updateReturnRequestMapper.returnRequestMapper(returnRequest,
                updateReturnRequestDto);
        if (updatedReturnRequest.getStatus().equals(OrderStatus.RETURN_APPROVED)) {
            return "Return request approved successfully.";
        }
        return "Return request rejected.";
    }

}
