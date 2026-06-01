package com.gv.game_vault.games.repository;

import com.gv.game_vault.games.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}