package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Activity;
import com.mf.wetalker.infra.db.entity.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteAndActivities {

    private Site site;
    private List<Activity> activities;

}
