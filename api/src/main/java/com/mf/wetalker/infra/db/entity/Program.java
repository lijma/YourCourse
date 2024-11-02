package com.mf.wetalker.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("wetalk_program")
public class Program {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date stopAt;

    private Integer price;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger userId;

    private String img;

    private String detail;

    private Boolean deleted;


}
