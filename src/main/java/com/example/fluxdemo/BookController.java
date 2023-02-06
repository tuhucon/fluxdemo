package com.example.fluxdemo;

import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class BookController {

    private BookRepository bookRepository;

    private WebClient webClient;

    @GetMapping("/books")
    public Flux<Book> getBooks() {
        return bookRepository.findAll().log();
    }

    @PostMapping("/books")
    public Mono<Book> addBook(@RequestBody FlatBook flatBook) {
        Book book = new Book();
        book.setTitle(flatBook.getTitle());
        book.setIsbn(flatBook.getIsbn());
        book.setAuthorId(flatBook.getAuthorId());
        return bookRepository.save(book).log();
    }

    @PostMapping("/addBook")
    public Mono<Book> addNewBook(@RequestBody FlatBook flatBook) {
        return Mono.just(flatBook)
                .map(tmp -> {
                    Book book = new Book();
                    book.setTitle(tmp.getTitle());
                    book.setIsbn(tmp.getIsbn());
                    book.setAuthorId(tmp.getAuthorId());
                    return book;
                })
                .flatMap(book -> bookRepository.save(book))
                .onErrorMap(e -> {
                    if (e instanceof DuplicateKeyException) {
                        return new ISBNDuplicatedException();
                    } else {
                        return e;
                    }
                })
                .log();
    }

    @PostMapping("/createBook")
    public Mono<Book> createBook() {
        Mono<String> googleContenct = webClient.get()
                .uri("https://google.com.vn")
                .retrieve()
                .bodyToMono(String.class).log();
        Mono<Book> result = googleContenct.flatMap(body -> {
                    Book book = new Book();
                    book.setTitle("google");
                    book.setContent(body);
                    book.setIsbn("google");
                    return bookRepository.save(book);
                }).log();
        return result;
    }
}
