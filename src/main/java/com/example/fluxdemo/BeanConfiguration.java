package com.example.fluxdemo;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class BeanConfiguration {

    @Bean
    HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                .followRedirect(true)
                .doOnConnected(con -> con
                        .addHandlerLast(new ReadTimeoutHandler(10))
                        .addHandlerLast(new WriteTimeoutHandler(10)));
    }

    @Bean
    WebClient getWebClient(HttpClient httpClient) {
       return WebClient.builder()
               .codecs(config -> config.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
               .clientConnector(new ReactorClientHttpConnector(httpClient))
               .filter((request, next) -> {
                   System.out.println("Filter in HttpClient: " + request + " at thread: " + Thread.currentThread().getName());
                   return next.exchange(request);
               })
               .build();
    }
}
