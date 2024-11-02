package com.mf.wetalker.infra.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("wetalk_activity")
public class Activity {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @TableField(value = "start_at")
    @JsonDeserialize(using = LocalDateFromEpochDeserializer.class)
    private LocalDate startAt;

    private String detail;

    private Integer audiences;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger userId;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger siteId;

    private String siteName;

    private Boolean deleted;

    private String img;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createAt;

    public Integer getAudiences() {
        return audiences != null ? audiences : 0;
    }

    private String votes;
}
