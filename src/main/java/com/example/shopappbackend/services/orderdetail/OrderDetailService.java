package com.example.shopappbackend.services.orderdetail;

import com.example.shopappbackend.dtos.OrderDetailDTO;
import com.example.shopappbackend.entities.Order;
import com.example.shopappbackend.entities.OrderDetail;
import com.example.shopappbackend.entities.Product;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.OrderDetailRepository;
import com.example.shopappbackend.services.order.IOrderService;
import com.example.shopappbackend.services.order.OrderService;
import com.example.shopappbackend.services.product.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService{
    private final IProductService productService;
    private final IOrderService orderService;
    private final OrderDetailRepository orderDetailRepository;

    private final ModelMapper modelMapper;
    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return findOrderDetail(id);
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) throws DataNotFoundException {
        Order existingOrder = orderService.getOrderById(orderId);
        List<OrderDetail> orderDetail = orderDetailRepository.findByOrderId(orderId);
        return orderDetail;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        Order existingOrder = orderService.getOrderById(orderDetailDTO.getOrderId());
        Product existingProduct = productService.getProductById(orderDetailDTO.getProductId());

        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .unitPrice(existingProduct.getUnitPrice())
                .totalAmount(orderDetailDTO.getNumberOfProducts() * existingProduct.getUnitPrice())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = findOrderDetail(id);
        Order existingOrder = orderService.getOrderById(orderDetailDTO.getOrderId());
        Product existingProduct = productService.getProductById(orderDetailDTO.getProductId());
        // Tạo ánh xạ

        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setUnitPrice(existingProduct.getUnitPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalAmount(existingProduct.getUnitPrice() * orderDetailDTO.getNumberOfProducts());

        return orderDetailRepository.save(existingOrderDetail);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteOrderDetail(Long id) throws DataNotFoundException {
        OrderDetail existingOrderDetail = findOrderDetail(id);
        orderDetailRepository.deleteById(id);
    }

    private OrderDetail findOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .filter(orderDetail -> !orderDetail.isDeleted())
                .orElseThrow(
                        () -> new DataNotFoundException("Order detail not found")
                );
    }
}
