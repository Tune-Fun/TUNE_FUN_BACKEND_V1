package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VotePaperRepository extends MongoRepository<VotePaperMongoEntity, String>, VotePaperCustomRepository {

    Optional<VotePaperMongoEntity> findByVoteEndAtBeforeAndAuthor(LocalDateTime voteEndAt, String author);

    Optional<VotePaperMongoEntity> findByVoteEndAtBeforeAndId(LocalDateTime voteEndAt, String id);

}
