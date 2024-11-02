package com.mf.wetalker.model;

import com.mf.wetalker.infra.auth.UserContextHolder;
import com.mf.wetalker.infra.db.entity.UserProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramJoinRequest {

    private BigInteger programId;
    private String userName;
    private String email;

    public UserProgram toProgram(){
        return UserProgram.builder()
                .userId(UserContextHolder.getUserId())
                .programId(this.programId)
                .userName(userName)
                .email(email)
                .build();
    }

}
