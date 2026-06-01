package com.gv.game_vault.games.repository;


import com.gv.game_vault.games.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByTitleIgnoreCase(String title);

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           LEFT JOIN FETCH g.genres
           """)
    List<Game> findAllWithGenres();

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           LEFT JOIN FETCH g.genres
           WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))
           """)
    List<Game> findByTitleWithGenres(@Param("title") String title);

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           LEFT JOIN FETCH g.genres
           WHERE g.price = :price
           """)
    List<Game> findByPriceWithGenres(@Param("price") BigDecimal price);

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           LEFT JOIN FETCH g.genres
           WHERE g.releaseYear = :year
           """)
    List<Game> findByReleaseYearWithGenres(@Param("year") Integer year);

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           JOIN g.genres genre
           LEFT JOIN FETCH g.genres
           WHERE LOWER(genre.name) = LOWER(:genreName)
           """)
    List<Game> findByGenreWithGenres(@Param("genreName") String genreName);

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           JOIN g.genres genre
           LEFT JOIN FETCH g.genres
           WHERE LOWER(genre.name) = LOWER(:genreName)
           AND g.price = :price
           """)
    List<Game> findByGenreAndPriceWithGenres(
            @Param("genreName") String genreName,
            @Param("price") BigDecimal price
    );
}
