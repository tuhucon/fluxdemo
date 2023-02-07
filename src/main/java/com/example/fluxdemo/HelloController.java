package com.example.fluxdemo;

import ch.qos.logback.core.joran.conditional.ThenAction;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    @AllArgsConstructor
    public static class Worker extends Thread {

        private MonoSink<String> sink;

        @Override
        public void run() {
            System.out.println("Worker run in thread: " + this.getName());
            try {
                Thread.sleep(30_000L);
            } catch (InterruptedException e) {
                sink.error(new RuntimeException(e));
            }
            sink.success("hello world");
        }
    }

    @GetMapping("/persons")
    public Flux<Person> getPersons() {
        System.out.println("start request at Thread: " + Thread.currentThread().getName());
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("tu hu con", 30));
        persons.add(new Person("chich choe", 40));

        return Flux.fromIterable(persons).log();
    }

    @PostMapping("/persons")
    public Mono<Person> addPerson(@RequestParam String name, @RequestParam Integer age) {
        System.out.println("start request at Thread: " + Thread.currentThread().getName());
        return Mono.just(new Person(name, age)).log();
    }

    @GetMapping("/hello")
    public Mono<String> helloWorld() throws InterruptedException {
        System.out.println("start request at Thread: " + Thread.currentThread().getName());

        return Mono.<String>create(sink -> {
            System.out.println("Start triggering consumer method at thread: " + Thread.currentThread().getName());
            Worker worker = new Worker(sink);
            worker.start();
        }).log();

    }

    @GetMapping("/context")
    public Mono<String> context() {
        return Mono.<String>deferContextual(ctx -> Mono.just(ctx.get("tuhucon"))).log();
    }

}
