package com.mf.wetalker.model;

import com.mf.wetalker.infra.db.entity.Program;
import com.mf.wetalker.infra.db.entity.UserProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramAndUsers {

    private Program program;
    private List<UserProgram> list;

}
