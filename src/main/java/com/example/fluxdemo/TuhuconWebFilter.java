package com.example.fluxdemo;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TuhuconWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().add("X-Tuhucon", "Tu hu con");
        return chain.filter(exchange).contextWrite(ctx -> ctx.put("tuhucon", "tuhucon"));
    }
}
