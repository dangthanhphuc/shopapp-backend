package com.example.shopappbackend.services.order;

import com.example.shopappbackend.dtos.CartItemDTO;
import com.example.shopappbackend.dtos.OrderDTO;
import com.example.shopappbackend.entities.*;
import com.example.shopappbackend.exceptions.DataNotFoundException;
import com.example.shopappbackend.repositories.OrderDetailRepository;
import com.example.shopappbackend.repositories.OrderRepository;
import com.example.shopappbackend.repositories.UserRepository;
import com.example.shopappbackend.services.coupon.ICouponService;
import com.example.shopappbackend.services.orderdetail.IOrderDetailService;
import com.example.shopappbackend.services.product.IProductService;
import com.example.shopappbackend.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService{
    private final IUserService userService;
    private final IProductService productService;
    private final ICouponService couponService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final ModelMapper modelMapper;

    @Override
    public Order getOrderById(Long orderId) throws DataNotFoundException {
        return getOrder(orderId);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) throws DataNotFoundException {
        List<Order> orders = orderRepository.findByUserId(userId).stream()
                .filter(order -> !order.isDeleted())
                .toList();
        if(orders.isEmpty()){
            throw new DataNotFoundException("Cannot find user id: " + userId);
        }
        return orders;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // Kiểm tra sự tồn tại của user
        User existingUser = userService.getUser(orderDTO.getUserId());

        // Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // Tạo Order
        Order newOrder = new Order();
        modelMapper.map(orderDTO, newOrder);
        newOrder.setUser(existingUser);
        newOrder.setShippingDate(LocalDate.now().plusDays(5));

        // Kiểm tra coupon
        if(orderDTO.getCouponCode() != null){
            try{
                Coupon existingCoupon = couponService.getCouponByCode(orderDTO.getCouponCode());
                newOrder.setCoupon(existingCoupon);
            }catch(Exception e) {
                newOrder.setCoupon(null);
            }
        }

        // Lưu order
        orderRepository.save(newOrder);

        // Tạo danh sách OrderDetail từ List<CartItemsDTO>
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        for(CartItemDTO cartItem : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);

            // Kiểm tra product có tồn tại không ?
            Product existingProduct = productService.getProductById(cartItem.getProductId());
            orderDetail.setProduct(existingProduct);

            orderDetail.setNumberOfProducts(cartItem.getQuantity());
            orderDetail.setUnitPrice(existingProduct.getUnitPrice());
            orderDetail.setTotalAmount(existingProduct.getUnitPrice() * cartItem.getQuantity());

            orderDetails.add(orderDetail);
        }

        orderDetailRepository.saveAll(orderDetails);
        // Set orderDetails cho newOrder
        newOrder.setOrderDetails(orderDetails);
        return newOrder;
    }

    @Override
    public Order updateOrder(Long orderId, OrderDTO orderDTO) throws DataNotFoundException {
        Order existingOrder = getOrder(orderId);
        User existingUser = userService.getUser(orderDTO.getUserId());
        // Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        modelMapper.map(orderDTO, existingOrder);

        existingOrder.setUser(existingUser);

        // Kiểm tra coupon
        if(orderDTO.getCouponCode() != null){
            try{
                Coupon existingCoupon = couponService.getCouponByCode(orderDTO.getCouponCode());
                existingOrder.setCoupon(existingCoupon);
            }catch(Exception e) {
                existingOrder.setCoupon(null);
            }
        }

//        for (Long orderDetailId : existingOrder.getOrderDetails().stream().map(OrderDetail::getId).toList()){
//            orderDetailRepository.deleteById(orderDetailId);
//        }

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long orderId) throws DataNotFoundException {
        Order existingOrder = getOrder(orderId);
        existingOrder.setDeleted(true);
        orderRepository.save(existingOrder);
    }

    @Override
    public Page<Order> getOrdersByKeyword(String keyword, PageRequest request) {
        return orderRepository.findByKeyword(keyword, request);
    }

    private Order getOrder(Long orderId) throws DataNotFoundException {
        return orderRepository.findById(orderId).stream()
                .filter(order -> !order.isDeleted())
                .findFirst()
                .orElseThrow(
                        () -> new DataNotFoundException("Order not found")
                );
    }
}
