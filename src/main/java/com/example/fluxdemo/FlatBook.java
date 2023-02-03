package com.example.fluxdemo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlatBook {

    Long id;
    String title;
    String isbn;

    @JsonProperty("author_id")
    Long authorId;
}
