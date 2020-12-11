package com.jgxq.common.res;

import com.jgxq.common.dto.PlayerInfo;
import com.jgxq.common.dto.PlayerTeam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerTeamRes {

    private String name;

    private List<PlayerTeam> playerList;

}
