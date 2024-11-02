package com.mf.wetalker.model;

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
public class ActivityJoinRequest {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger roleId;
    private BigInteger activityId;
    private String userName;
    private String email;

}
