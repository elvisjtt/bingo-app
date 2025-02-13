package com.bingo.bingogame.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.bingo.bingogame.repository.BingoCardRepository;
import com.bingo.model.BingoCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BingoCardService {

    private final BingoCardRepository bingoCardRepository;

    public BingoCardService(BingoCardRepository bingoCardRepository) {
        this.bingoCardRepository = bingoCardRepository;
    }

    public Mono<BingoCard> generateCard(UUID gameId) {
        int[][] grid = generateGrid();
        String numbersString = convertGridToString(grid);
        BingoCard card = new BingoCard();
        card.setGameId(gameId);
        card.setNumbersJson(numbersString);

        return bingoCardRepository.save(card);
    }

    private int[][] generateGrid() {
        int[][] grid = new int[5][5];

        for (int col = 0; col < 5; col++) {
            int min = col * 15 + 1;
            int max = min + 14;

            List<Integer> numbers = IntStream.rangeClosed(min, max)
                    .boxed()
                    .collect(Collectors.toList());
            Collections.shuffle(numbers);

            for (int row = 0; row < 5; row++) {
                grid[row][col] = (row == 2 && col == 2) ? 0 : numbers.remove(0);
            }
        }

        return grid;
    }

    private String convertGridToString(int[][] grid) {
        return Arrays.stream(grid)
                .map(row -> Arrays.stream(row)
                        .mapToObj(String::valueOf)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }

    public Flux<BingoCard> listCardsByGameId(UUID gameId) {
        return bingoCardRepository.findByGameId(gameId);
    }

}
