package com.bingo.bingogame.controller;

import com.bingo.bingogame.service.BingoCardService;
import com.bingo.bingogame.service.BingoGameService;
import com.bingo.bingogame.service.BingoNumberService;
import com.bingo.model.BingoGame;
import com.bingo.model.BingoNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BingoGameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private BingoGameService bingoGameService;

    @Mock
    private BingoCardService bingoCardService;

    @Mock
    private BingoNumberService bingoNumberService;

    @InjectMocks
    private BingoGameController bingoGameController;

    private UUID gameId;

    @BeforeEach
    public void setUp() {
        webTestClient.post().uri("/api/bingo/game")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BingoGame.class)
                .value(bingoGame -> {
                    assertNotNull(bingoGame.getId(), "El gameId no debe ser null despues de crear el BingoGame");
                    this.gameId = bingoGame.getId();
                });
    }

    @Test
    public void testApiBingoGamePost() {
        webTestClient.post().uri("/api/bingo/game")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BingoGame.class)
                .value(bingoGame -> {
                    assertNotNull(bingoGame.getId(), "El gameId no debe ser null despues de crear el BingoGame");
                    this.gameId = bingoGame.getId();
                });
    }

    @Test
    public void testGenerateBingoCard() {
        assertNotNull(gameId, "El gameId debe asignarse antes de crear en BigoCard");
        webTestClient.post()
                .uri("/api/bingo/{gameId}/cards", gameId)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testCallBingoNumber() {
        assertNotNull(gameId, "El gameId debe asignarse antes de llamar callBingoNumber");
        webTestClient.post()
                .uri("/api/bingo/{gameId}/numbers", gameId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BingoNumber.class)
                .value(bingoNumber -> {
                    assertNotNull(bingoNumber, "El numero de bingo no debe ser null");
                    assertTrue(bingoNumber.getNumber() > 0 && bingoNumber.getNumber() <= 75,
                            "El numero llamado debe estar entre 1 y 75");
                });
    }

}
