package com.gv.game_vault.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public List<Game> getGamesByTitle(String title) {
        return gameRepository.findBytitle(title);
    }

    public List<Game> getGameByGenre(String genreName) {

        return gameRepository.findByGenres_name(genreName);
    }

    public List<Game> getGamesByPrice(BigDecimal price) {
        return gameRepository.findByPrice(price);
    }

    public List<Game> getGameByYear(Integer year) {
        return gameRepository.findByReleaseYear(year);
    }

    public List<Game> getGamesByPrice_Genre(String genreName, BigDecimal price) {
        return gameRepository.findByGenres_nameAndPrice(genreName, price);
    }

    public Game addGame(Game game) {
        List<Game> gameOptional = gameRepository.findBytitle(game.getTitle());
        if (!gameOptional.isEmpty()) {
            throw new IllegalStateException("Game already exists");
        }
        gameRepository.save(game);
        return game;
    }

    public Game updateGame(Game Updatedgame) {
        List<Game> existingGame = gameRepository.findBytitle(Updatedgame.getTitle());
        if (!existingGame.isEmpty()) {
            Game gameToUpdate = existingGame.getFirst();
            gameToUpdate.setTitle(Updatedgame.getTitle());
            gameToUpdate.setPrice(Updatedgame.getPrice());
            gameToUpdate.setReleaseYear(Updatedgame.getReleaseYear());
            gameToUpdate.setGenres(Updatedgame.getGenres());

            gameRepository.save(gameToUpdate);
            return gameToUpdate;
        }
        return null;
    }

    public void deleteGame(Long id) {
        boolean canDelete = gameRepository.existsById(id);
        if (!canDelete) {
            throw new IllegalStateException("Game does not exist");
        }
        gameRepository.deleteById(id);
    }
}
