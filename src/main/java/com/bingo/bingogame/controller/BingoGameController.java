package com.bingo.bingogame.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.bingo.api.ApiApi;
import com.bingo.bingogame.service.BingoCardService;
import com.bingo.bingogame.service.BingoGameService;
import com.bingo.bingogame.service.BingoNumberService;
import com.bingo.model.BingoCard;
import com.bingo.model.BingoCardCheckRequest;
import com.bingo.model.BingoCardCheckResponse;
import com.bingo.model.BingoGame;
import com.bingo.model.BingoNumber;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BingoGameController implements ApiApi {

    private final BingoGameService bingoGameService;
    private final BingoCardService bingoCardService;
    private final BingoNumberService bingoNumberService;

    public BingoGameController(BingoGameService bingoGameService, BingoCardService bingoCardService,
            BingoNumberService bingoNumberService) {
        this.bingoGameService = bingoGameService;
        this.bingoCardService = bingoCardService;
        this.bingoNumberService = bingoNumberService;
    }

    public Mono<ResponseEntity<BingoGame>> apiBingoGamePost(ServerWebExchange exchange) {
        return bingoGameService.createGame()
                .map(juego -> ResponseEntity.status(HttpStatus.CREATED).body(juego));
    }

    public Mono<ResponseEntity<BingoCard>> generateBingoCard(
            @PathVariable String gameId,
            ServerWebExchange exchange) {
        try {
            UUID id = UUID.fromString(gameId);
            return bingoCardService.generateCard(id)
                    .map(card -> ResponseEntity.status(HttpStatus.CREATED).body(card));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

    }

    public Mono<ResponseEntity<Flux<BingoCard>>> listBingoCards(@PathVariable String gameId,
            ServerWebExchange exchange) {

        try {
            UUID id = UUID.fromString(gameId);

            return bingoCardService.listCardsByGameId(id)
                    .collectList()
                    .map(cards -> ResponseEntity.ok(Flux.fromIterable(cards)));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    public Mono<ResponseEntity<BingoNumber>> callBingoNumber(@PathVariable String gameId,
            ServerWebExchange exchange) {
        try {
            UUID id = UUID.fromString(gameId);

            return bingoNumberService.callNumber(id)
                    .map(bingoNumber -> ResponseEntity.ok(bingoNumber))
                    .defaultIfEmpty(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    public Mono<ResponseEntity<Flux<BingoNumber>>> getBingoNumbers(String gameId, ServerWebExchange exchange) {
        try {
            UUID id = UUID.fromString(gameId);

            Flux<BingoNumber> numbers = bingoNumberService.getBingoNumbers(id);
            return Mono.just(ResponseEntity.ok(numbers));

        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    public Mono<ResponseEntity<Flux<BingoGame>>> listBingoGames(ServerWebExchange exchange) {
        try {
            return bingoGameService.getAllBingoGames()
                    .collectList()
                    .map(games -> ResponseEntity.ok(Flux.fromIterable(games)));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }

    public Mono<ResponseEntity<BingoCardCheckResponse>> checkBingoCard(
            @Valid @RequestBody Mono<BingoCardCheckRequest> bingoCardCheckRequest,
            ServerWebExchange exchange) {

        return bingoCardCheckRequest.flatMap(request -> {
            UUID cardId = request.getCardId();
            UUID gameId = request.getGameId();
            return bingoGameService.checkBingoCard(cardId, gameId)
                    .map(response -> ResponseEntity.ok(response))
                    .defaultIfEmpty(ResponseEntity.badRequest().build());
        });
    }
}
