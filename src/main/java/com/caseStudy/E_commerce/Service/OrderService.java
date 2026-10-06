package com.caseStudy.E_commerce.Service;


import com.caseStudy.E_commerce.DTO.Order.OrderRequestDTO;
import com.caseStudy.E_commerce.DTO.Order.OrderResponseDTO;
import com.caseStudy.E_commerce.DTO.OrderItem.OrderItemRequestDTO;
import com.caseStudy.E_commerce.DTO.OrderItem.OrderItemResponseDTO;
import com.caseStudy.E_commerce.Entity.Order;
import com.caseStudy.E_commerce.Entity.OrderItem;
import com.caseStudy.E_commerce.Entity.Product;
import com.caseStudy.E_commerce.Entity.User;
import com.caseStudy.E_commerce.ExceptionHandler.InsufficientStockException;
import com.caseStudy.E_commerce.ExceptionHandler.ResourceNotFoundException;
import com.caseStudy.E_commerce.Mapper.OrderItemMapper;
import com.caseStudy.E_commerce.Mapper.OrderMapper;
import com.caseStudy.E_commerce.Mapper.UserMapper;
import com.caseStudy.E_commerce.Repository.OrderRepository;
import com.caseStudy.E_commerce.Repository.ProductRepository;
import com.caseStudy.E_commerce.Repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    public OrderService(OrderRepository orderRepository,
                         UserRepository userRepository ,
                         OrderMapper orderMapper , ProductRepository productRepository,
                         OrderItemMapper orderItemMapper){
        this.orderRepository=orderRepository;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.orderItemMapper = orderItemMapper;
    }



    @Transactional
    public OrderResponseDTO createOrder(Long userId,OrderRequestDTO orderRequestDTO){
        // order request me mujhe userid or
        User user = userRepository.
                findById(userId).
                orElseThrow(()-> new ResourceNotFoundException("The user with Id : " +
                        userId + " not exist"));
        Order order = orderMapper.mapToEntity(orderRequestDTO,user);
        int quantity = 0;
        BigDecimal price = BigDecimal.ZERO;
        //total Quantity
        //total amount
        for (OrderItemRequestDTO ele : orderRequestDTO.getItems()){
            // check for productId
            Product product = productRepository.findById(ele.getProductId()).
                    orElseThrow(()-> new ResourceNotFoundException("Product with Id : " +
                            ele.getProductId() + "Don't exist"));

            if(ele.getQuantity()<=product.getQuantity()){
                quantity = quantity + ele.getQuantity();
                product.setQuantity(product.getQuantity()-ele.getQuantity());
            }
            else{
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            price = price.add(
                    product.getPrice().multiply(BigDecimal.valueOf(ele.getQuantity()))
            );
            OrderItem orderItem = orderItemMapper.mapToEntity(ele,product,order);
            order.getOrderItems().add(orderItem);
        }
        order.setTotalQuantity(quantity);
        order.setTotalAmount(price);
        user.addOrder(order);
        orderRepository.save(order);
        return orderMapper.mapToResponseDTO(order);
    }
    // get orderHistory
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getOrderHistory(Long userId, Pageable pageable){
        // validate the user
        userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User with Id : " + userId + " don't exist"));
        //findAllOrderByUserId
        // have to make pagebale because user pagaeble wouldn't have
        Pageable newPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        // ishme mujhe
        return orderRepository.
                findOrderHistory(userId,newPageable).
                map(order -> orderMapper.mapToResponseDTO(order));
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long userId, Long orderId){
        Order order = orderRepository.
                findOrderByUserIdAndOrderId(userId,orderId).orElseThrow(()->
                        new ResourceNotFoundException(
                                "Order with Id : " + orderId +
                                        " don't exist Under User with Id :" + userId));
        return orderMapper.mapToResponseDTO(order);
    }





}
