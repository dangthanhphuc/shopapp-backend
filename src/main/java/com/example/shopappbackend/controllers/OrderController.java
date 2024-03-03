package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.OrderDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.order.OrderListResponse;
import com.example.shopappbackend.responses.order.OrderResponse;
import com.example.shopappbackend.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
@RestController
public class OrderController {
    private final IOrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getOrderById(
            @PathVariable Long id
    ) throws DataNotFoundException {

            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Successfully get order by id !")
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .data(OrderResponse.fromOrder(order))
                            .build()
            );

    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getOrdersByUserId(
            @PathVariable("user_id") Long id
    ) throws DataNotFoundException {

        List<Order> ordersByUserId = orderService.getOrdersByUserId(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get orders !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(ordersByUserId.stream().map(OrderResponse::fromOrder).toList())
                        .build()
        );

    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Input is invalid !")
                            .status(HttpStatus.BAD_REQUEST)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .data(errors)
                            .build()
            );
        }
        Order order = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully create order !")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(OrderResponse.fromOrder(order))
                        .build()
        );

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) throws DataNotFoundException {

            if(result.hasErrors()) {
                List<String> errors = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Order updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully updated order !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(OrderResponse.fromOrder(updatedOrder))
                        .build()
        );

    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> deleteOrderById(@PathVariable("id") Long orderId) throws DataNotFoundException {

        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully deleted order !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
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
