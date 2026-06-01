package com.gv.game_vault.games.repository;


import com.gv.game_vault.games.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByTitleIgnoreCase(String title);
    Boolean existsByTitleIgnoreCase(String title);

    List<Game> findByGenres_nameIgnoreCase(String genreName);
    List<Game> findByPrice(BigDecimal price);
    List<Game> findByReleaseYear(Integer year);
    List<Game> findByGenres_nameAndPrice(String genreName, BigDecimal price);

}
