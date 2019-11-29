package com.keyboard.arifbd2221.keyboardexample.model;

public class FilterWords {
    public String userId;
    public String word;
    public Integer quantity;

    public FilterWords(){

    }

    public FilterWords(String userId, String word, Integer quantity) {
        this.userId = userId;
        this.word = word;
        this.quantity = quantity;
    }
}
