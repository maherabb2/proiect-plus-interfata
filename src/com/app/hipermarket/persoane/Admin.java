package com.app.hipermarket.persoane;

public class Admin {
    private String user;
    private String parola;

    public Admin(String user, String parola) {
        this.user = user;
        this.parola = parola;
    }

    public String getUser() {
        return user;
    }

    public String getParola() {
        return parola;
    }
}
