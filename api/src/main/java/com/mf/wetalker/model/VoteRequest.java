package com.mf.wetalker.model;

import com.mf.wetalker.infra.auth.UserContextHolder;
import com.mf.wetalker.infra.db.entity.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {

    private List<Vote> votes;

    public List<Vote> toVotes(BigInteger activityId) {
        List<Vote> voteList = new ArrayList<>();
        votes.forEach(item -> {
            Vote vote = Vote.builder()
                    .activityId(activityId)
                    .userId(UserContextHolder.getUserId())
                    .question(item.getQuestion())
                    .answer(item.getAnswer())
                    .build();
            voteList.add(vote);
        });

        return voteList;
    }
}
