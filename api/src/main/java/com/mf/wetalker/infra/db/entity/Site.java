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
@TableName("wetalk_site")
public class Site {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

    private String siteName;
    private String email;
    private String city;
    private String logo;
    private String detail;

}
