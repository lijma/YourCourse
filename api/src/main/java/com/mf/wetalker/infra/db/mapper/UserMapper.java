package com.mf.wetalker.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mf.wetalker.infra.db.entity.WeTalkUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<WeTalkUser> {

}

