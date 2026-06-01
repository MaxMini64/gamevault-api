package com.gv.game_vault.games.dto;

import java.math.BigDecimal;
import java.util.Set;

public record GameResponse(
        Long id,
        String title,
        BigDecimal price,
        Integer releaseYear,
        Set<GenreResponse> genres
) {
}