package com.app.hipermarket.persoane;

import com.app.hipermarket.products.Produs;

import java.util.ArrayList;

public class Client {
    private ArrayList<Produs> produse = new ArrayList<>();

    public void adaugaProdus(Produs produs) {
        produse.add(produs);
    }

    public boolean removeProdus(int id) {
        for (Produs produs: produse) {
            if (produs.getId() == id) {
                produse.remove(produs);
                return true;
            }
        }

        return false;
    }

    public void removeProduse() {
        produse.removeAll(produse);
    }

    public double totalDePlata() {
        double suma = 0;

        for(Produs produs : produse) {
            suma += produs.getPret();
        }

        return suma;
    }

    public void afisareProduse() {
        System.out.println("Lista produselor din cos: ");
        for (Produs produs : produse) {
            System.out.println(produs.toString());
        }
    }
}
