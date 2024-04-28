package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VotePaperRepository extends MongoRepository<VotePaperMongoEntity, String> {

    Optional<VotePaperMongoEntity> findByVoteEndAtAfterAndAuthor(LocalDateTime voteEndAt, String author);

    Optional<VotePaperMongoEntity> findByVoteEndAtAfterAndId(LocalDateTime voteEndAt, String id);

}
