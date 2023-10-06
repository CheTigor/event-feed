package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    //Широта
    @Min(-90)
    @Max(90)
    private Float lat;
    //Долгота
    @Min(-180)
    @Max(180)
    private Float lon;
}
