package com.app.hipermarket.parser;

import com.app.hipermarket.products.Cantitate;
import com.app.hipermarket.products.Categorie;
import com.app.hipermarket.products.Produs;

import java.util.ArrayList;

public class ProductParser {
    public Produs parse(String rawData) {
        Produs produs = null;

        if (rawData != null && !rawData.isEmpty()) {
            String[] items = rawData.split(";");

            int id = Integer.parseInt(items[0]);
            String nume = items[1];
            double pret = Double.parseDouble(items[2]);
            double quantity = Double.parseDouble(items[3]);
            Cantitate cantitate = toCantitate(Integer.parseInt(items[4]));
            Categorie categorie = toCategorie(Integer.parseInt(items[5]));

            produs = new Produs(id, nume, pret, cantitate, categorie);
        }

        return produs;
    }

    public ArrayList<Produs> parseAll(String data) {
        ArrayList<Produs> products = new ArrayList<>();

        for (String element: data.split("\n")) {
            Produs product = parse(element);
            if(product != null) {
                products.add(product);
            }
        }

        return products;
    }

    private Cantitate toCantitate(int cantitate) {
        switch (cantitate) {
            case 0: return Cantitate.KG;
            case 1: return Cantitate.L;
            case 2: return Cantitate.BUC;
            default: return Cantitate.KG;
        }
    }

    private Categorie toCategorie(int categorie) {
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
