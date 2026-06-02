package com.gv.game_vault.games.controller;

import com.gv.game_vault.games.dto.GameResponse;
import com.gv.game_vault.games.dto.GameRequest;
import com.gv.game_vault.games.service.GameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year)
    {
        List<GameResponse> games = gameService.getGames(title, genre, price, year);
        return ResponseEntity.ok(games);
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody GameRequest request) {
        GameResponse gameCreated = gameService.addGame(request);
        return new ResponseEntity<>(gameCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameRequest request)
    {
        GameResponse gameUpdated = gameService.updateGame(id, request);
        return ResponseEntity.ok(gameUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id)
    {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
