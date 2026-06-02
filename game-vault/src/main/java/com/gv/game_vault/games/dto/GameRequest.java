package com.gv.game_vault.games.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record GameRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price,

        @NotNull(message = "Release year is required")
        @Min(value = 1970, message = "Release year must be greater than or equal to 1970")
        @Max(value = 2100, message = "Release year must be realistic")
        Integer releaseYear,

        @NotEmpty(message = "At least one genre is required")
        Set<Long> genreIds
) {
}