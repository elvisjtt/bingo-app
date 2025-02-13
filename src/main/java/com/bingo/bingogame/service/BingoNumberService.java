package com.bingo.bingogame.service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

import com.bingo.bingogame.repository.BingoGameRepository;
import com.bingo.bingogame.repository.BingoNumberRepository;
import com.bingo.model.BingoNumber;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BingoNumberService {

    private final BingoGameRepository bingoGameRepository;
    private final BingoNumberRepository bingoNumberRepository;

    public BingoNumberService(BingoGameRepository bingoGameRepository, BingoNumberRepository bingoNumberRepository) {
        this.bingoGameRepository = bingoGameRepository;
        this.bingoNumberRepository = bingoNumberRepository;
    }

    public Mono<BingoNumber> callNumber(UUID gameId) {
        return bingoGameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found")))
                .flatMap(game -> {
                    return bingoNumberRepository.findByGameId(gameId)
                            .map(BingoNumber::getNumber)
                            .collectList()
                            .flatMap(calledNumbers -> {
                                AtomicInteger atomicNumber = new AtomicInteger();
                                Random rand = new Random();
                                int number;
                                do {
                                    number = rand.nextInt(75) + 1;
                                } while (calledNumbers.contains(number));
                                atomicNumber.set(number);
                                BingoNumber bingoNumber = new BingoNumber();
                                bingoNumber.setNumber(atomicNumber.get());
                                bingoNumber.setGameId(gameId);
                                bingoNumber.setColumn(getColumn(atomicNumber.get()));

                                return bingoNumberRepository.save(bingoNumber)
                                        .flatMap(savedBingoNumber -> {
                                            String updatedCalledNumbers = game.getCalledNumbers();
                                            if (!updatedCalledNumbers.isEmpty()) {
                                                updatedCalledNumbers += "," + atomicNumber.get();
                                            } else {
                                                updatedCalledNumbers = String.valueOf(atomicNumber.get());
                                            }
                                            game.setCalledNumbers(updatedCalledNumbers);
                                            return bingoGameRepository.save(game)
                                                    .then(Mono.just(savedBingoNumber));
                                        });
                            });
                });
    }

    public Flux<BingoNumber> getBingoNumbers(UUID gameId) {
        return bingoNumberRepository.findByGameId(gameId);
    }

    private BingoNumber.ColumnEnum getColumn(int number) {
        if (number >= 1 && number <= 15) {
            return BingoNumber.ColumnEnum.B;
        } else if (number >= 16 && number <= 30) {
            return BingoNumber.ColumnEnum.I;
        } else if (number >= 31 && number <= 45) {
            return BingoNumber.ColumnEnum.N;
        } else if (number >= 46 && number <= 60) {
            return BingoNumber.ColumnEnum.G;
        } else {
            return BingoNumber.ColumnEnum.O;
        }
    }

}
