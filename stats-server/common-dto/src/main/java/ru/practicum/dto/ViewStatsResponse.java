package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsResponse {

    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotNull
    private Long hits;

}
