package com.jacob_araujo.turing_chat_api.exception;

public class RecipientAndDataUniqueViolationException extends RuntimeException {
    public RecipientAndDataUniqueViolationException(String message) {
        super(message);
    }
}
