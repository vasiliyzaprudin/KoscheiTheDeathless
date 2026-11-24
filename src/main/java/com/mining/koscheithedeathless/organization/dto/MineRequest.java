package com.mining.koscheithedeathless.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MineRequest {
    @NotBlank(message = "Код рудника обязателен.")
    @Size(max = 50)
    private String mine_code;

    @NotBlank(message = "Название обязательно.")
    private String mine_name;

    private String mine_address;
    private String mine_description;
}