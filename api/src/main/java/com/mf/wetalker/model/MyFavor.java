package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Favor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyFavor {

    private Map<String, List<Favor>> favors;

}
