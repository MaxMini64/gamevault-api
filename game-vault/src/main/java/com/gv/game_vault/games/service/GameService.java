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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<GameResponse> getGames(
            String title,
            String genre,
            BigDecimal price,
            Integer year,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Long> gameIdsPage;

        if (genre != null && price != null) {
            gameIdsPage = gameRepository.findIdsByGenreAndPrice(genre, price, pageable);
        } else if (price != null) {
            gameIdsPage = gameRepository.findIdsByPrice(price, pageable);
        } else if (title != null) {
            gameIdsPage = gameRepository.findIdsByTitle(title, pageable);
        } else if (year != null) {
            gameIdsPage = gameRepository.findIdsByReleaseYear(year, pageable);
        } else if (genre != null) {
            gameIdsPage = gameRepository.findIdsByGenre(genre, pageable);
        } else {
            gameIdsPage = gameRepository.findAllIds(pageable);
        }

        List<Long> ids = gameIdsPage.getContent();

        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Game> games = gameRepository.findAllByIdsWithGenres(ids);

        List<GameResponse> responses = games.stream()
                .map(GameMapper::toGameResponse)
                .toList();

        return new PageImpl<>(
                responses,
                pageable,
                gameIdsPage.getTotalElements()
        );
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
