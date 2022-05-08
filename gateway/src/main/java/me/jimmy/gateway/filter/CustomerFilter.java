package me.jimmy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class CustomerFilter extends AbstractGatewayFilterFactory<CustomerFilter.Config> {
    public CustomerFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // custom pre filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Custom Pre filter : request uri : {}", request.getId());
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Customer POST filter : response code -> {}", response.getStatusCode());
            }));
        });
    }

    static class Config {
        // 여기서 configuration 정보를 넣는다.
    }
}
