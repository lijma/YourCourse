package com.mf.wetalker.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("wetalk_user_program")
public class UserProgram {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger programId;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger userId;

    private String userName;

    private String email;

}
