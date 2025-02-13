package com.bingo.bingogame.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.bingo.model.BingoGame;

import reactor.core.publisher.Mono;

@Repository
public interface BingoGameRepository extends ReactiveCrudRepository<BingoGame, UUID> {

    Mono<BingoGame> findById(UUID id);
}
