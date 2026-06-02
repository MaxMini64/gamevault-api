package com.gv.game_vault.games.repository;

import com.gv.game_vault.games.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByTitleIgnoreCase(String title);

    @Query("""
           SELECT g.gameId
           FROM Game g
           """)
    Page<Long> findAllIds(Pageable pageable);

    @Query("""
           SELECT g.gameId
           FROM Game g
           WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%'))
           """)
    Page<Long> findIdsByTitle(@Param("title") String title, Pageable pageable);

    @Query("""
           SELECT g.gameId
           FROM Game g
           WHERE g.price = :price
           """)
    Page<Long> findIdsByPrice(@Param("price") BigDecimal price, Pageable pageable);

    @Query("""
           SELECT g.gameId
           FROM Game g
           WHERE g.releaseYear = :year
           """)
    Page<Long> findIdsByReleaseYear(@Param("year") Integer year, Pageable pageable);

    @Query("""
           SELECT DISTINCT g.gameId
           FROM Game g
           JOIN g.genres genre
           WHERE LOWER(genre.name) = LOWER(:genreName)
           """)
    Page<Long> findIdsByGenre(@Param("genreName") String genreName, Pageable pageable);

    @Query("""
           SELECT DISTINCT g.gameId
           FROM Game g
           JOIN g.genres genre
           WHERE LOWER(genre.name) = LOWER(:genreName)
           AND g.price = :price
           """)
    Page<Long> findIdsByGenreAndPrice(
            @Param("genreName") String genreName,
            @Param("price") BigDecimal price,
            Pageable pageable
    );

    @Query("""
           SELECT DISTINCT g
           FROM Game g
           LEFT JOIN FETCH g.genres
           WHERE g.gameId IN :ids
           """)
    List<Game> findAllByIdsWithGenres(@Param("ids") List<Long> ids);
}