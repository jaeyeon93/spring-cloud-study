package me.jimmy.orderservice.controller;

import me.jimmy.orderservice.dto.OrderDto;
import me.jimmy.orderservice.dto.RequestOrder;
import me.jimmy.orderservice.dto.ResponseOrder;
import me.jimmy.orderservice.entity.OrderEntity;
import me.jimmy.orderservice.kafka.KafkaProducer;
import me.jimmy.orderservice.kafka.OrderProducer;
import me.jimmy.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    private Environment env;
    private OrderService orderService;
    private KafkaProducer producer;
    private OrderProducer orderProducer;

    public OrderController(Environment env, OrderService orderService, KafkaProducer producer, OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.producer = producer;
        this.orderProducer = orderProducer;
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder request) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(request, OrderDto.class);
        orderDto.setUserId(userId);

        // jpa
//        OrderDto createDto = orderService.createOrder(orderDto);
//        ResponseOrder returnValue = modelMapper.map(createDto, ResponseOrder.class);

        // kafka
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(request.getUnitPrice() * request.getQty());

        // send this order to the kafka
        producer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        ResponseOrder returnValue = modelMapper.map(orderDto, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderList.forEach(order -> result.add(new ModelMapper().map(order, ResponseOrder.class)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("Order-service running on port %s", env.getProperty("local.server.port"));
    }
}
