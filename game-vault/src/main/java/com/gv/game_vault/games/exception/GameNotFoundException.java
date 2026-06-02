package com.gv.game_vault.games.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Game with id " + id + " was not found");
    }
}