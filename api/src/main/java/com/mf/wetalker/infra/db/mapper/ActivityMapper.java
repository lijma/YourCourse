package com.mf.wetalker.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.wetalker.infra.db.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

    List<Activity> selectName(@Param("content") String content);

    void updateVote(BigInteger activityId, String votes);

}


