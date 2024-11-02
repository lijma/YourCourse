package com.mf.wetalker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mf.wetalker.infra.auth.UserContextHolder;
import com.mf.wetalker.infra.db.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramAndLabelsRequest {

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

    private List<LabelRequest> labels;

    public Program getProgram() {
        return Program.builder()
                .title(title)
                .startAt(startAt)
                .endAt(endAt)
                .stopAt(stopAt)
                .price(price)
                .userId(UserContextHolder.getUserId())
                .img(img)
                .detail(detail)
                .build();
    }

    public static ProgramAndLabelsRequest from(Program program) {
        return ProgramAndLabelsRequest.builder()
                .title(program.getTitle())
                .startAt(program.getStartAt())
                .endAt(program.getEndAt())
                .stopAt(program.getStopAt())
                .price(program.getPrice())
                .userId(UserContextHolder.getUserId())
                .img(program.getImg())
                .detail(program.getDetail())
                .build();
    }


}
