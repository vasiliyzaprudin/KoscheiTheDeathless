package com.mining.koscheithedeathless.organization.dto;

import com.mining.koscheithedeathless.organization.Mine;
import lombok.Data;

@Data
public class MineResponse {
    private Long mine_id;
    private String mine_code;
    private String mine_name;
    private boolean mine_enabled;
    private String mine_address;
    private String mine_description;

    public static MineResponse from(Mine mine) {
        MineResponse response = new MineResponse();
        response.setMine_id(mine.getMine_id());
        response.setMine_code(mine.getMine_code());
        response.setMine_name(mine.getMine_name());
        response.setMine_enabled(mine.isMine_enabled());
        response.setMine_address(mine.getMine_address());
        response.setMine_description(mine.getMine_description());
        return response;
    }
}