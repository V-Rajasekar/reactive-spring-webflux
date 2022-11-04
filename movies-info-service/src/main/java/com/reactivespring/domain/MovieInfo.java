package com.reactivespring.domain;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;
    @NotBlank(message = "movieinfo.name is EMPTY")
    private String name;

    @NotNull
    @Positive(message = "movieinfo.year must me a positive value")
    private Integer year;
    private List<@NotBlank(message = "movieinfo.cast is Required") String> cast;
    private LocalDate release_date;
}
