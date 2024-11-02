package com.mf.wetalker.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.wetalker.infra.db.entity.Site;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SiteMapper extends BaseMapper<Site> {
    List<Site> selectName(@Param("content") String content);
}

