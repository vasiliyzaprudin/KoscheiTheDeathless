package com.mining.koscheithedeathless.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MineRequest {
    @NotBlank(message = "Код рудника обязателен")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "Название обязательно")
    private String name;

    private String address;
    private String description;
}