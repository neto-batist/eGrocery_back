package com.egrocery_back.errors;

public class WrongPassword extends Exception {

    public WrongPassword(){
        super("Wrong Password.");
        System.out.println("User tried to login whit wrong password");
    }

}
