package com.tune_fun.v1.vote.adapter.output.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteChoiceRepository extends MongoRepository<VoteChoiceMongoEntity, String> {
}
