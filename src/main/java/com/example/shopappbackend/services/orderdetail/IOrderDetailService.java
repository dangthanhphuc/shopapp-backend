package com.example.shopappbackend.services.orderdetail;

import com.example.shopappbackend.dtos.OrderDetailDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.entities.OrderDetail;
import com.example.shopappbackend.exceptions.DataNotFoundException;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    List<OrderDetail> getOrderDetailByOrderId(Long orderId) throws DataNotFoundException;
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id ,OrderDetailDTO orderDetailDTO) throws DataNotFoundException;
    void deleteOrderDetail(Long id) throws DataNotFoundException;

}
