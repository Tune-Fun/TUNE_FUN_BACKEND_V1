package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.QuerydslRepositorySupport;

public class VotePaperCustomRepositoryImpl extends QuerydslRepositorySupport implements VotePaperCustomRepository {

    private static final QVotePaperMongoEntity VOTE_PAPER = QVotePaperMongoEntity.votePaperMongoEntity;

    /**
     * Creates a new {@link QuerydslRepositorySupport} for the given {@link MongoOperations}.
     *
     * @param operations must not be {@literal null}.
     */
    public VotePaperCustomRepositoryImpl(MongoOperations operations) {
        super(operations);
    }

}
