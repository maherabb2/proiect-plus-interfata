package com.app.hipermarket.persoane;

public class Manager {
    private String user;
    private String parola;

    public Manager(String user, String parola) {
        this.user = user;
        this.parola = parola;
    }

    @Override
    public String toString() {
        return user + ";" + parola + "\n";
    }

    public String getUser() {
        return user;
    }

    public String getParola() {
        return parola;
    }

    @Override
    public boolean equals(Object obj) {
        Manager m = (Manager) obj;

        return this.user.equals(m.user) && this.parola.equals(m.parola);
    }

    public String toFile() {
        return user + ";" + parola + "\n";
    }

    public Object[] toArray() {
        return new Object[] { user, parola };
    }
}
