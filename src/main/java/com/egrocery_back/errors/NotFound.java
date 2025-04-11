package com.egrocery_back.errors;

public class NotFound extends Exception {
    public NotFound(String entity, Integer id) {
        super(entity + " Not found whit id: "+id);
        System.out.println(entity + " Not found whit id: "+id);
    }
}
