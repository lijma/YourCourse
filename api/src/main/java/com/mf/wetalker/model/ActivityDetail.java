package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Activity;
import com.mf.wetalker.infra.db.entity.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDetail {

    private Activity activity;
    private List<UserActivity> roles;
    private List<UserActivity> audiences;

}
