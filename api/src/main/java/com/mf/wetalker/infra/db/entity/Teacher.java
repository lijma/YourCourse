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
@TableName("wetalk_teacher")
public class Teacher {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

    private String teacherName;
    private String email;
    private String city;
    private String logo;
    private String detail;
    private String url;
    private String status;
    private String comment;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger userId;

}
