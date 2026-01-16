package com.gv.game_vault.games;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @Column(name = "genre_id")
    private Long genreID;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore // JsonIgnore es para que no se quede en un ciclo infinito al buscar entre la relacion
    private Set<Game> games = new HashSet<>();

    public Genre() {
    }

    public Genre(Long genreID, String name) {
        this.genreID = genreID;
        this.name = name;
    }

    public Long getGenreID() {
        return genreID;
    }

    public void setGenreID(Long genreID) {
        this.genreID = genreID;
    }

    public String getGenreName() {
        return name;
    }

    public void setGenreName(String name) {
        this.name = name;
    }
}
