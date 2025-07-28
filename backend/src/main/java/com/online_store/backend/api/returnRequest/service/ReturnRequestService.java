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

/**
 * Service class for handling customer return requests.
 * This service manages the creation of new return requests by users and the
 * approval or rejection of these requests by sellers.
 */
@Service
@RequiredArgsConstructor
public class ReturnRequestService {
    private final ReturnRequestRepository returnRequestRepository;
    private final OrderRepository orderRepository;
    private final CommonUtilsService commonUtilsService;
    private final CreateReturnRequestMapper createReturnRequestMapper;
    private final UpdateReturnRequestMapper updateReturnRequestMapper;
    private CompanyUtilsService companyUtilsService;

    /**
     * Creates a new return request for a specific order item.
     * It first validates that the order item belongs to the current user before
     * creating the request.
     *
     * @param dto The DTO containing the order item ID and reason for the return.
     * @return A success message upon successful creation of the return request.
     * @throws EntityNotFoundException if the specified order item is not found or
     *                                 doesn't belong to the user.
     * @see com.online_store.backend.api.returnRequest.controller.ReturnRequestController#createReturnRequest(ReturnRequestDto)
     */
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

    /**
     * Updates the status of a return request.
     * This method is intended for sellers to approve or reject a return request.
     * It validates that the current user's company is the one that sold the
     * product.
     *
     * @param id                     The ID of the return request to update.
     * @param updateReturnRequestDto The DTO containing the new status.
     * @return A message indicating whether the request was approved or rejected.
     * @throws EntityNotFoundException if the return request is not found.
     * @throws Error                   if the current user's company does not have
     *                                 permission to update the request.
     * @see com.online_store.backend.api.returnRequest.controller.ReturnRequestController#updateReturnRequestStatus(Long,
     *      UpdateReturnRequestDto)
     */
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
