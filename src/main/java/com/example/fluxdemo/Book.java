package com.example.fluxdemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    public Long id;

    public String title;

    public String isbn;

    public String content;

    public Long authorId;

}
