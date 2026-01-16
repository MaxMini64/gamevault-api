package com.gv.game_vault.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/game")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAllGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year){
        if (genre != null && price != null) {
            return gameService.getGamesByPrice_Genre(genre, price);
        }
        else if (price != null) {
            return gameService.getGamesByPrice(price);
        }
        else if (title != null) {
            return gameService.getGamesByTitle(title);
        }
        else if (year != null) {
            return gameService.getGameByYear(year);
        }
        else if (genre != null) {
            return gameService.getGameByGenre(genre);
        }
        else {
            return gameService.getGames();
        }
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        try {
            Game gameCreated = gameService.addGame(game);
            return new ResponseEntity<>(gameCreated, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity<Game> updateGame(@RequestBody Game game) {
        Game gameUpdated = gameService.updateGame(game);
        if (gameUpdated != null) {
            return new ResponseEntity<>(gameUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gameID}")
    public ResponseEntity<String> deleteGame(@PathVariable Long gameID) {
        gameService.deleteGame(gameID);
        return new ResponseEntity<>("Game deleted successfully", HttpStatus.OK);
    }

}
