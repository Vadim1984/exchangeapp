package com.example.exchangeapp.exceptions;

public class ExchangeException extends RuntimeException {
    public ExchangeException(String message){
        super(message);
    }

    public ExchangeException(Throwable cause){
        super(cause);
    }
}
