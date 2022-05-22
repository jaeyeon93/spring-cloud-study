package me.jimmy.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jimmy.orderservice.dto.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String, String> template;

    List<Field> fields = Arrays.asList(
            new Field("string", true, "order_id"),
            new Field("string", true, "product_id"),
            new Field("string", true, "user_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price")
    );
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("orders")
            .build();

    public OrderProducer(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .user_id(orderDto.getUserId())
                .product_id(orderDto.getProductId())
                .qty(orderDto.getQty())
                .total_price(orderDto.getUnitPrice() * orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .build();
        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            log.error("json error : {}", e);
        }

        template.send(topic, json);
        log.info("Kafka Producer sent order payload data from the order microservice : {}", kafkaOrderDto);

        return orderDto;
    }
}
