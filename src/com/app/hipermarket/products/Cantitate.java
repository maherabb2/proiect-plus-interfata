package com.app.hipermarket.products;

public enum Cantitate {
    KG,
    L,
    BUC;

    public static Cantitate toCantitate(int cantitate) {
        switch (cantitate) {
            case 0: return Cantitate.KG;
            case 1: return Cantitate.L;
            case 2: return Cantitate.BUC;
            default: return Cantitate.KG;
        }
    }

}