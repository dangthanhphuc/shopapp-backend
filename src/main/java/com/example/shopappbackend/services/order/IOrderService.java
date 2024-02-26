package com.example.shopappbackend.services.order;

import com.example.shopappbackend.dtos.OrderDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order getOrderById(Long orderId) throws DataNotFoundException;
    List<Order> getOrdersByUserId(Long userId) throws DataNotFoundException;
    Order createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    Order updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long orderId) throws DataNotFoundException;
    Page<Order> getOrdersByKeyword(String keyword, PageRequest request);
}
