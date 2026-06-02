package com.gv.game_vault.games.exception;

public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String title) {
        super("Game with title '" + title + "' already exists");
    }
}