package com.gv.game_vault.games.service;

import com.gv.game_vault.games.dto.GameRequest;
import com.gv.game_vault.games.dto.GameResponse;
import com.gv.game_vault.games.entity.Game;
import com.gv.game_vault.games.entity.Genre;
import com.gv.game_vault.games.exception.GameAlreadyExistsException;
import com.gv.game_vault.games.exception.GameNotFoundException;
import com.gv.game_vault.games.exception.GenreNotFoundException;
import com.gv.game_vault.games.mapper.GameMapper;
import com.gv.game_vault.games.repository.GameRepository;
import com.gv.game_vault.games.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;

    public GameService(GameRepository gameRepository, GenreRepository genreRepository) {
        this.gameRepository = gameRepository;
        this.genreRepository = genreRepository;
    }

    public List<GameResponse> getGames(String title, String genre ,BigDecimal price, Integer year) {
        List<Game> games;

        if (genre != null && price != null) {
            games = gameRepository.findByGenreAndPriceWithGenres(genre, price);
        } else if (price != null) {
            games = gameRepository.findByPriceWithGenres(price);
        } else if (title != null) {
            games = gameRepository.findByTitleWithGenres(title);
        } else if (year != null) {
            games = gameRepository.findByReleaseYearWithGenres(year);
        } else if (genre != null) {
            games = gameRepository.findByGenreWithGenres(genre);
        } else {
            games = gameRepository.findAllWithGenres();
        }

        return games.stream()
                .map(GameMapper::toGameResponse)
                .toList();
    }

    public GameResponse addGame(GameRequest request) {
        boolean gameExists = gameRepository.existsByTitleIgnoreCase(request.title());

        if(gameExists){
            throw new GameAlreadyExistsException(request.title());
        }

        Set<Genre> genres = getGenresByIds(request.genreIds());

        Game game = new Game();
        game.setTitle(request.title());
        game.setPrice(request.price());
        game.setReleaseYear(request.releaseYear());
        game.setGenres(genres);

        Game savedGame = gameRepository.save(game);
        return GameMapper.toGameResponse(savedGame);
    }

    public GameResponse updateGame(Long id, GameRequest request) {
        Game gameToUpdate = gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));

        Set<Genre> genres = getGenresByIds(request.genreIds());

        gameToUpdate.setTitle(request.title());
        gameToUpdate.setPrice(request.price());
        gameToUpdate.setReleaseYear(request.releaseYear());
        gameToUpdate.setGenres(genres);

        Game updatedGame = gameRepository.save(gameToUpdate);

        return GameMapper.toGameResponse(updatedGame);
    }

    public void deleteGame(Long id) {
        boolean exists = gameRepository.existsById(id);
        if (!exists) {
            throw new GameNotFoundException(id);
        }

        gameRepository.deleteById(id);
    }

    private Set<Genre> getGenresByIds(Set<Long> genreIds){
        List<Genre> genres = genreRepository.findAllById(genreIds);

        if(genres.size() != genreIds.size()){
            throw new GenreNotFoundException();
        }

        return new HashSet<>(genres);
    }
}
