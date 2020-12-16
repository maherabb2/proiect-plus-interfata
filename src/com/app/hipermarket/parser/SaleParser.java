package com.app.hipermarket.parser;

import com.app.hipermarket.products.Vanzare;

import java.util.ArrayList;

public class SaleParser {
    public Vanzare parse(String rawData) {
        Vanzare sale = null;

        if (rawData != null && !rawData.isEmpty()) {
            double total = Double.parseDouble(rawData);
            sale = new Vanzare(total);
        }

        return sale;
    }

    public ArrayList<Vanzare> parseAll(String data) {
        ArrayList<Vanzare> sales = new ArrayList<>();

        for (String element: data.split("\n")) {
            Vanzare sale = parse(element);
            if(sale != null) {
                sales.add(sale);
            }
        }

        return sales;
    }
}
