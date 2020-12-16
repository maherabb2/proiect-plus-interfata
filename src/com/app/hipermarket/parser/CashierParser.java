package com.app.hipermarket.parser;

import com.app.hipermarket.persoane.Manager;
import com.app.hipermarket.products.Produs;

import java.util.ArrayList;

public class CashierParser {
    public Manager parse(String rawData) {
        Manager cashier = null;

        if (rawData != null && !rawData.isEmpty()) {
            String[] items = rawData.split(";");

            String username = items[0];
            String password = items[1];
            cashier = new Manager(username, password);
        }

        return cashier;
    }

    public ArrayList<Manager> parseAll(String data) {
        ArrayList<Manager> cashiers = new ArrayList<>();

        for (String element: data.split("\n")) {
            Manager cashier = parse(element);
            if(cashier != null) {
                cashiers.add(cashier);
            }
        }

        return cashiers;
    }
}
