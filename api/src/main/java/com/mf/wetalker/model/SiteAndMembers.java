package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Site;
import com.mf.wetalker.infra.db.entity.UserSite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteAndMembers {

    private Site site;
    private List<UserSite> siteUsers;

}
