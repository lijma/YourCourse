package com.mf.wetalker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
public class ProgramAndLabels {

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

    private List<String> labels;

    public static ProgramAndLabels from(Program program) {
        ProgramAndLabels programAndLabels = new ProgramAndLabels();
        programAndLabels.setTitle(program.getTitle());
        programAndLabels.setStartAt(program.getStartAt());
        programAndLabels.setEndAt(program.getEndAt());
        programAndLabels.setStopAt(program.getStopAt());
        programAndLabels.setPrice(program.getPrice());
        programAndLabels.setUserId(program.getUserId());
        programAndLabels.setImg(program.getImg());
        programAndLabels.setDetail(program.getDetail());
        return programAndLabels;
    }


}
