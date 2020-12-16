package com.app.hipermarket.menu;

import com.app.hipermarket.parser.AdminParser;
import com.app.hipermarket.parser.CashierParser;
import com.app.hipermarket.parser.SaleParser;
import com.app.hipermarket.products.Vanzare;
import com.app.hipermarket.readers.DatabaseReader;
import com.app.hipermarket.readers.DatabaseWriter;
import com.app.hipermarket.Constants;
import com.app.hipermarket.persoane.*;
import com.app.hipermarket.persoane.Cashiers;
import com.app.hipermarket.parser.ProductParser;
import com.app.hipermarket.products.Products;
import com.app.hipermarket.products.Produs;

import java.util.ArrayList;

public class ClientMenu extends Menu {
    private final String title;
    private final Client client;
    private Manager manager;
    private double suma;

    public ClientMenu(String title) {
        super();
        this.title = title;
        this.client = new Client();
        this.manager = null;
    }

    @Override
    public void display() {
        System.out.println(formatHeader());
        System.out.println(formatTilte(title));
        System.out.println(formatHeader());

        System.out.println("1 - Scaneaza un produs ");
        System.out.println("2 - Finalizare plata");
        System.out.println("4 - Total de plata");
        System.out.println("5 - Sterge un produs");
        System.out.println("6 - Anulare cumparaturi");
        System.out.println("8 - Interogare casier");
        System.out.println("9 - Interogare admin");
        System.out.println("0 - Inapoi la meniul principal");
    }

    public Menu interpretCommand(char c) {
        menu = this;

        switch (c) {
            case '1':
                scanProduct();
                break;
            case '2':
                startPayment();
                break;
            case '4':
                totalCost();
                break;
            case '5':
                deleteProduct();
                break;
            case '6':
                cancel();
                break;
            case '8':
                managerInquire();
                break;
            case '9':
                adminInquire();
                break;
            case '0':
                menu = new MainMenu();
                break;
            default:
                System.out.println("Invalid command");
        }

        return menu;
    }

    public void cancel() {
        client.removeProduse();
        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(true));
    }

    private void scanProduct() {
        DatabaseReader reader = new DatabaseReader();

        String[] items = reader.read(Constants.MESSAGE_FILE).toString().split(";");

        int productId = Integer.parseInt(items[0]);
        System.out.println(this.getClass() + ": received productId: " + productId);

        double productQuantity = Double.parseDouble(items[1]);
        System.out.println(this.getClass() + ": received quantity: " + productQuantity);

        StringBuilder rawProducts = reader.read(Constants.PRODUCTS_FILE);
        ProductParser productParser = new ProductParser();
        ArrayList<Produs> productsList = productParser.parseAll(rawProducts.toString());
        Products products = new Products(productsList);
        Produs product = products.findProduct(productId);

        if (product != null) {
            if (productQuantity != 0) {
                product.updatePrice(productQuantity);
            }

            client.adaugaProdus(product);

            DatabaseWriter writer = new DatabaseWriter(Constants.OUTPUT_FILE);
            writer.write(product.toString());
        }
        else {
            System.out.println(this.getClass() + ": produsul cu id-ul " + productId + " nu a fost gasit.");
        }
    }

    private void startPayment() {
        DatabaseReader reader = new DatabaseReader();
        double amount = Double.parseDouble(reader.read(Constants.MESSAGE_FILE).toString());
        System.out.println(this.getClass() + ": received amount of : " + amount + "RON.");

        suma -= amount;

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(suma));

        if (suma <= 0) {
            Vanzare vanzare = new Vanzare(client.totalDePlata());

            DatabaseReader databaseReader = new DatabaseReader();
            StringBuilder rawSales = databaseReader.read(Constants.SALES_FILE);

            SaleParser saleParser = new SaleParser();
            ArrayList<Vanzare> saleList = saleParser.parseAll(rawSales.toString());

            saleList.add(vanzare);

            DatabaseWriter salesWriter = new DatabaseWriter(Constants.SALES_FILE);
            salesWriter.writeAll(saleList);
        }
    }

    public void  totalCost() {
        String total  = String.valueOf(client.totalDePlata());
        DatabaseWriter databaseWriter =  new DatabaseWriter(Constants.OUTPUT_FILE);
        suma = client.totalDePlata();
        databaseWriter.write(total);
    }

    public void deleteProduct() {
        DatabaseReader databaseReader = new DatabaseReader();
        int productId = Integer.parseInt(databaseReader.read(Constants.MESSAGE_FILE).toString());
        System.out.println(this.getClass() + ": received productId: " + productId);

        boolean removed = false;
        if (manager != null) {
            removed = client.removeProdus(productId);
        }

        DatabaseWriter writer = new DatabaseWriter(Constants.OUTPUT_FILE);
        writer.write(String.valueOf(removed));
    }

    public void managerInquire() {
        DatabaseReader databaseReader = new DatabaseReader();
        String readValue = databaseReader.read(Constants.MESSAGE_FILE).toString();
        CashierParser cashierParser = new CashierParser();
        Manager cashier = cashierParser.parse(readValue);

        StringBuilder rawCashiers = databaseReader.read(Constants.CASHIERS_FILE);
        ArrayList<Manager> cashierList = cashierParser.parseAll(rawCashiers.toString());
        Cashiers cashiers = new Cashiers(cashierList);
        Manager result = cashiers.findProduct(cashier.getUser());

        boolean returnValue = false;
        if (result != null) {
            if(result.getParola().equals(cashier.getParola())) {
                this.manager = cashier;
                returnValue = true;
            }
        }

        DatabaseWriter databaseWriter =  new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(returnValue));
    }

    public void adminInquire() {
        DatabaseReader databaseReader = new DatabaseReader();
        String readValue = databaseReader.read(Constants.MESSAGE_FILE).toString();
        AdminParser adminParser = new AdminParser();
        Admin admin = adminParser.parse(readValue);

        StringBuilder rawAdmins = databaseReader.read(Constants.ADMINS_FILE);
        ArrayList<Admin> adminList = adminParser.parseAll(rawAdmins.toString());
        Admins admins = new Admins(adminList);
        Admin result = admins.findAdmin(admin.getUser());

        boolean returnValue = false;
        if (result != null) {
            if(result.getParola().equals(admin.getParola())) {
                returnValue = true;
            }
        }

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(returnValue));
    }
}



