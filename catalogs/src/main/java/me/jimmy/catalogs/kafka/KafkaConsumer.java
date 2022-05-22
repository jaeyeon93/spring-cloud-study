package me.jimmy.catalogs.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jimmy.catalogs.entity.CatalogEntity;
import me.jimmy.catalogs.repository.CatalogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    private CatalogRepository repository;

    public KafkaConsumer(CatalogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.info("kafka message : {}", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("parsing error : {}", e);
        }

        CatalogEntity entity = repository.findByProductId((String) map.get("productId"));

        if (entity != null) {
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
            repository.save(entity);
        }
    }
}
