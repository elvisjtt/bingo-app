package com.bingo.bingogame.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.bingo.model.BingoNumber;

import reactor.core.publisher.Flux;

@Repository
public interface BingoNumberRepository extends ReactiveCrudRepository<BingoNumber, Integer> {

    Flux<BingoNumber> findByGameId(UUID gameId);
}