package com.app.hipermarket.products;

public enum Categorie {
    FRUCTE,
    LEGUME,
    MEZELURI,
    LACTATE,
    PANIFICATIE,
    CURATENIE,
    BAUTURI;

    public static Categorie toCategorie(int categorie) {
        switch (categorie) {
            case 0: return Categorie.FRUCTE;
            case 1: return Categorie.LEGUME;
            case 2: return Categorie.MEZELURI;
            case 3: return Categorie.LACTATE;
            case 4: return Categorie.PANIFICATIE;
            case 5: return Categorie.CURATENIE;
            case 6: return Categorie.BAUTURI;
            default: return Categorie.FRUCTE;
        }
    }

}