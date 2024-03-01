package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.OrderDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.responses.order.OrderListResponse;
import com.example.shopappbackend.responses.order.OrderResponse;
import com.example.shopappbackend.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
@RestController
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getOrderById(@PathVariable Long id){
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(OrderResponse.fromOrder(order));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("user_id") Long id){
        try {
            List<Order> ordersByUserId = orderService.getOrdersByUserId(id);
            return ResponseEntity.ok(ordersByUserId.stream().map(OrderResponse::fromOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(OrderResponse.fromOrder(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ){
        try {
            if(result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Order updatedOrder = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(OrderResponse.fromOrder(updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<String> deleteOrderById(@PathVariable("id") Long orderId){
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Deleted order by id " + orderId + " successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/get-order-by-keywords")
    public ResponseEntity<?> getOrderByKeywords(
        @RequestParam(defaultValue = "", required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int limit
    ){
        // Tạo PageRequest từ page và limit
        PageRequest pageRequest = PageRequest.of(
                page, limit
                , Sort.by("id").ascending()
        );
        // Tìm kiếm danh sách page theo keyword và theo pageRequest
        Page<OrderResponse> orderPage= orderService
                .getOrdersByKeyword(keyword, pageRequest)
                .map(OrderResponse::fromOrder);

        // Lấy tổng số trang
        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orderResponses = orderPage.getContent();

        OrderListResponse orderListResponse = OrderListResponse.builder()
                .orders(orderResponses)
                .totalPages(totalPages)
                .build();

        return ResponseEntity.ok().body(orderListResponse);
    }
}
