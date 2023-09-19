package ru.practicum.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {

    private String app;
    private String uri;
    private Long hits;

}
