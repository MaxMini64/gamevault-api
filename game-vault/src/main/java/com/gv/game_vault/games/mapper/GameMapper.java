package com.gv.game_vault.games.mapper;

import com.gv.game_vault.games.dto.GameResponse;
import com.gv.game_vault.games.dto.GenreResponse;
import com.gv.game_vault.games.entity.Game;
import com.gv.game_vault.games.entity.Genre;

import java.util.Set;
import java.util.stream.Collectors;

public class GameMapper {

    public static GameResponse toGameResponse(Game game) {
        return new GameResponse(
                game.getGameId(),
                game.getTitle(),
                game.getPrice(),
                game.getReleaseYear(),
                toGenreResponseSet(game.getGenres())
        );
    }

    private static Set<GenreResponse> toGenreResponseSet(Set<Genre> genres) {
        return genres.stream()
                .map(GameMapper::toGenreResponse)
                .collect(Collectors.toSet());
    }

    private static GenreResponse toGenreResponse(Genre genre) {
        return new GenreResponse(
                genre.getGenreID(),
                genre.getGenreName()
        );
    }
}