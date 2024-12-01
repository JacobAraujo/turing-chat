package com.jacob_araujo.turing_chat_api.exception;

public class invalidTokenException extends RuntimeException {
    public invalidTokenException(String message) {
        super(message);
    }
}
