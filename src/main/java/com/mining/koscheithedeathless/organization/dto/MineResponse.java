package com.mining.koscheithedeathless.organization.dto;

import com.mining.koscheithedeathless.organization.Mine;
import lombok.Data;

@Data
public class MineResponse {
    private Long mineId;
    private String mineCode;
    private String mineName;
    private boolean mineEnabled;
    private String mineAddress;
    private String mineDescription;

    public static MineResponse from(Mine mine) {
        MineResponse response = new MineResponse();
        response.setMineId(mine.getMineId());
        response.setMineCode(mine.getMineCode());
        response.setMineName(mine.getMineName());
        response.setMineEnabled(mine.isMineEnabled());
        response.setMineAddress(mine.getMineAddress());
        response.setMineDescription(mine.getMineDescription());
        return response;
    }
}