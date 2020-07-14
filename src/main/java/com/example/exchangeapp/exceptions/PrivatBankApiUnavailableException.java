package com.example.exchangeapp.exceptions;

public class PrivatBankApiUnavailableException extends RuntimeException {
    public PrivatBankApiUnavailableException(String message){
        super(message);
    }

    public PrivatBankApiUnavailableException(Throwable cause){
        super(cause);
    }
}
