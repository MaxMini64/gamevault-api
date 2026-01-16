package com.gv.game_vault.games;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.title = ?1")
    Optional<Game> findByTitle(String title);

    List<Game> findBytitle(String title);
    List<Game> findByGenres_name(String genreName);
    List<Game> findByPrice(BigDecimal price);
    List<Game> findByReleaseYear(Integer year);
    List<Game> findByGenres_nameAndPrice(String genreName, BigDecimal price);
}
