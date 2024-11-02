package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAndProgram {

    private String userName;
    private String email;
    private Program program;


}
