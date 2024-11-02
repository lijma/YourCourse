package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {

    private List<UserActivity> roles;
    private BigInteger activityId;
    private String template;

    public List<UserActivity> toUserActivities() {
        List<UserActivity> activities = new ArrayList<>();
        roles.forEach(role -> {
            UserActivity userActivity = UserActivity.builder()
                    .activityId(activityId)
                    .role(role.getRole())
                    .roleText(role.getRoleText())
                    .userId(role.getUserId())
                    .userName(role.getUserName())
                    .email(role.getEmail())
                    .build();
            activities.add(userActivity);
        });

        return activities;
    }
}
