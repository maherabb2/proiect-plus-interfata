package com.app.hipermarket.products;

public class Produs {
    private int id;
    private String nume;
    private double pret;
    private Cantitate cantitate;
    private Categorie categorie;
    private double quantity;

    public Produs(int id, String nume, double pret, Cantitate cantitate, Categorie categorie) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.cantitate = cantitate;
        this.categorie = categorie;
        this.quantity = 1;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void updatePrice(double quantity) {
        if (cantitate != Cantitate.BUC) {
            this.pret = quantity * pret;
            this.quantity = quantity;
        }
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        Produs p = (Produs)obj;

        return this.id == p.id && this.nume.equals(p.nume)
                && this.pret == p.pret && this.cantitate == p.cantitate && this.categorie == p.categorie;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public double getPret() {
        return pret;
    }

    @Override
    public String toString() {
        return "" + id +
                ";" + nume +
                ";" + pret +
                ";" + quantity +
                ";" + cantitate.ordinal() +
                ";" + categorie.ordinal() + "\n";
    }

    public Object[] toArray() {
        return new Object[] { id, nume, pret, quantity, cantitate, categorie };
    }
}
