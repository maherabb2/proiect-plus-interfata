package com.app.hipermarket.menu;

public abstract class Menu {
    private final static int DISPLAY_SIZE = 64;
    protected Menu menu;

    public void display() {

    }

    public Menu interpretCommand(char c) {
        return null;
    }

    protected String formatTilte(String title) {
        int pipes = 2;
        int spaces = DISPLAY_SIZE - title.length() - pipes;
        String result = title;

        for (int i = 0; i < spaces / 2 ;i++) {
            result = " " + result + " ";
        }

        if (spaces % 2 == 1) {
            result += " ";
        }

        result = "|" + result + "|";
        return  result;
    }

    protected String formatHeader() {
        String header = "";
        for(int i = 0; i < DISPLAY_SIZE; i++) {
            header = header + "=";
        }

        return  header;
    }
}