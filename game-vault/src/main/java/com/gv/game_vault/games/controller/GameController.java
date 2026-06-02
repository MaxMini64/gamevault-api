package com.gv.game_vault.games.controller;

import com.gv.game_vault.games.dto.GameResponse;
import com.gv.game_vault.games.dto.GameRequest;
import com.gv.game_vault.games.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Games", description = "Endpoints for managing video games")
@RestController
@RequestMapping(path = "api/v1/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Get games", description = "Returns all games or filters them by title, genre, price, or release year")
    @GetMapping
    public ResponseEntity<Page<GameResponse>> getAllGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<GameResponse> games = gameService.getGames(title, genre, price, year, page, size);
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "Create a game", description = "Creates a new game with one or more existing genres")
    @PostMapping
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody GameRequest request) {
        GameResponse gameCreated = gameService.addGame(request);
        return new ResponseEntity<>(gameCreated, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a game", description = "Updates an existing game by ID")
    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(
            @PathVariable Long id,
            @Valid @RequestBody GameRequest request)
    {
        GameResponse gameUpdated = gameService.updateGame(id, request);
        return ResponseEntity.ok(gameUpdated);
    }

    @Operation(summary = "Delete a game", description = "Deletes an existing game by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id)
    {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

}
