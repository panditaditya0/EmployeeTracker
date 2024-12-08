package com.ppus.psl_user_heartbeat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    public Long count;
    public List<HearBeatModel> activeUsers;
}