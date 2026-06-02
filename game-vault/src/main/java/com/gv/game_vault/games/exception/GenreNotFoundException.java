package com.gv.game_vault.games.exception;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException() {
        super("One or more genres were not found");
    }
}