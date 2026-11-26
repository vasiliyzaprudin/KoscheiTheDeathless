package com.mining.koscheithedeathless.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MineRequest {
    @NotBlank(message = "Код рудника обязателен.")
    @Size(max = 50)
    private String mineCode;

    @NotBlank(message = "Название обязательно.")
    private String mineName;

    private String mineAddress;

    private String mineDescription;
}