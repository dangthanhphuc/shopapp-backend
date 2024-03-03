package com.example.shopappbackend.controllers;

import com.example.shopappbackend.dtos.OrderDetailDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.entities.OrderDetail;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.responses.ResponseObject;
import com.example.shopappbackend.responses.order.OrderDetailResponse;
import com.example.shopappbackend.services.order.IOrderService;
import com.example.shopappbackend.services.orderdetail.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
@RestController
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) throws DataNotFoundException {

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
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
        OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully create order detail !")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                        .build()
        );

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getOrderDetail(
            @Valid @PathVariable Long id
    ) throws DataNotFoundException {

        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get order detail by id !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                        .build()
        );

    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getOrderDetailsByOrderId(
            @Valid @PathVariable("orderId") Long orderId
    ) throws DataNotFoundException {

        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetailList.stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully get order detail by order id !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(orderDetailResponses)
                        .build()
        );

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateOrderDetail(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) throws DataNotFoundException {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .filter(error -> error.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
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
        OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully update order detail !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(OrderDetailResponse.fromOrderDetail(orderDetail))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> deleteOrderDetail(
            @Valid @PathVariable Long id
    ) throws DataNotFoundException {

        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Successfully deleted order detail !")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );

    }
}
