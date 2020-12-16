package com.app.hipermarket.products;

import java.util.ArrayList;

public class Products {
    private ArrayList<Produs> products;

    public Products(ArrayList<Produs> products) {
        this.products = products;
    }

    public Produs findProduct(int productId) {
        for(Produs product: products) {
            if(product.getId() == productId) {
                return product;
            }
        }

        return null;
    }
}
