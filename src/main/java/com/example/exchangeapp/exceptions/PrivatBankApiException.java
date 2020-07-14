package com.example.exchangeapp.exceptions;

public class PrivatBankApiException extends RuntimeException {
    public PrivatBankApiException(String message){
        super(message);
    }

    public PrivatBankApiException(Throwable cause){
        super(cause);
    }
}
