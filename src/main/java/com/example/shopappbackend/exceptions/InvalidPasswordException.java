package com.example.shopappbackend.exceptions;


public class InvalidPasswordException extends Exception{
    public InvalidPasswordException(String meessage){
        super(meessage);
    }
}
