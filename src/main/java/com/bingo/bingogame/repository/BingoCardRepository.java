package com.bingo.bingogame.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.bingo.model.BingoCard;

import reactor.core.publisher.Flux;

@Repository
public interface BingoCardRepository extends ReactiveCrudRepository<BingoCard, UUID> {

    Flux<BingoCard> findByGameId(UUID gameId);
}
