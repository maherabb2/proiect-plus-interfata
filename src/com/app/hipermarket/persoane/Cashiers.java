package com.app.hipermarket.persoane;

import com.app.hipermarket.persoane.Manager;

import java.util.ArrayList;

public class Cashiers {
    private ArrayList<Manager> cashiers;

    public Cashiers(ArrayList<Manager> cashiers) {
        this.cashiers = cashiers;
    }

    public Manager findProduct(String username) {
        for(Manager cashier: cashiers) {
            if(cashier.getUser().equals(username)) {
                return cashier;
            }
        }

        return null;
    }
}
