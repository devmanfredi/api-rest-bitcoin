package com.apirest.bitcoin.repository;

import com.apirest.bitcoin.domain.Extract;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractRepository extends ReactiveMongoRepository<Extract, String> {
}
