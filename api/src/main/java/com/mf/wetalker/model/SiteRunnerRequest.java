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
public class SiteRunnerRequest {

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger siteId;

    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger userId;

}
