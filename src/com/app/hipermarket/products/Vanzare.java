package com.app.hipermarket.products;

public class Vanzare {
    double suma;

    public double getSuma() {
        return suma;
    }

    public Vanzare(double suma) {
        this.suma = suma;
    }

    @Override
    public String toString() {
        return String.valueOf(suma) + "\n";
    }
}
