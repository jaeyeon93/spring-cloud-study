package me.jimmy.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jimmy.orderservice.dto.OrderDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private KafkaTemplate<String, String> template;

    public KafkaProducer(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";

        try {
            json = mapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            log.error("json error : {}", e);
        }

        template.send(topic, json);
        log.info("Kafka Producer sent data from the order microservice : {}", orderDto);

        return orderDto;
    }
}
