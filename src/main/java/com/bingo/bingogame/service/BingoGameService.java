package com.bingo.bingogame.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bingo.bingogame.repository.BingoCardRepository;
import com.bingo.bingogame.repository.BingoGameRepository;
import com.bingo.model.BingoCard;
import com.bingo.model.BingoCardCheckResponse;
import com.bingo.model.BingoGame;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BingoGameService {
    private final BingoGameRepository bingoGameRepository;
    private final BingoCardRepository bingoCardRepository;

    public BingoGameService(BingoGameRepository bingoGameRepository, BingoCardRepository bingoCardRepository) {
        this.bingoGameRepository = bingoGameRepository;
        this.bingoCardRepository = bingoCardRepository;
    }

    public Mono<BingoGame> createGame() {

        BingoGame game = new BingoGame();
        game.setStatus(BingoGame.StatusEnum.CREATED);
        game.calledNumbers("");
        return bingoGameRepository.save(game)
                .onErrorResume(e -> {
                    System.err.println("Error al crear juego: " + e.getMessage());
                    return Mono.just(new BingoGame());
                });
    }

    public Flux<BingoGame> getAllBingoGames() {
        return bingoGameRepository.findAll();
    }

    public Mono<BingoCardCheckResponse> checkBingoCard(UUID cardId, UUID gameId) {
        return Mono.zip(
                bingoCardRepository.findById(cardId),
                bingoGameRepository.findById(gameId))
                .flatMap(tuple -> {
                    BingoCard bingoCard = tuple.getT1();
                    BingoGame bingoGame = tuple.getT2();

                    if (bingoCard == null || bingoGame == null) {
                        BingoCardCheckResponse bingoCardCheckResponse = new BingoCardCheckResponse();
                        bingoCardCheckResponse.setIsWinner(false);
                        bingoCardCheckResponse.setMessage("Juego o cartilla no encontrados");
                        return Mono.just(bingoCardCheckResponse);
                    }

                    Set<Integer> cardNumbers = parseCardNumbers(bingoCard.getNumbersJson());
                    Set<Integer> calledNumbers = parseCalledNumbers(bingoGame.getCalledNumbers());

                    boolean isWinner = calledNumbers.containsAll(cardNumbers);
                    String message = isWinner ? "¡Felicidades, has ganado!" : "Todavía no has ganado, sigue jugando.";

                    BingoCardCheckResponse bingoCardCheckResponse = new BingoCardCheckResponse();
                    bingoCardCheckResponse.setIsWinner(isWinner);
                    bingoCardCheckResponse.setMessage(message);

                    return Mono.just(bingoCardCheckResponse);
                });
    }

    private Set<Integer> parseCardNumbers(String numbersJson) {
        Set<Integer> numbers = new HashSet<>();
        String[] rows = numbersJson.split(";");
        for (String row : rows) {
            String[] numbersInRow = row.split(",");
            for (String number : numbersInRow) {
                numbers.add(Integer.parseInt(number.trim()));
            }
        }
        return numbers;
    }

    private Set<Integer> parseCalledNumbers(String calledNumbers) {
        Set<Integer> numbers = new HashSet<>();
        String[] calledNumbersArray = calledNumbers.split(",");
        for (String number : calledNumbersArray) {
            numbers.add(Integer.parseInt(number.trim()));
        }
        return numbers;
    }

}
