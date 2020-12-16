package com.app.hipermarket.menu;

import com.app.hipermarket.parser.AdminParser;
import com.app.hipermarket.parser.ProductParser;
import com.app.hipermarket.readers.DatabaseReader;
import com.app.hipermarket.readers.DatabaseWriter;
import com.app.hipermarket.Constants;
import com.app.hipermarket.persoane.*;
import com.app.hipermarket.products.*;


import java.util.ArrayList;

public class ManagerMenu extends Menu {
    private final String title;

    public ManagerMenu(String title) {
        super();
        this.title = title;
    }

    @Override
    public void display() {
        System.out.println(formatHeader());
        System.out.println(formatTilte(title));
        System.out.println(formatHeader());

        System.out.println("1 - Adauga un produs");
        System.out.println("2 - Afiseaza produse dintr-o categorie");
        System.out.println("3 - Sterge un produs");
        System.out.println("0 - Inapoi la meniul principal");
    }

    public Menu interpretCommand(char c) {
        menu = this;

        switch (c) {
            case '1':
                addProduct();
                break;
            case '2':
                listProductsCategory();
                break;
            case '3':
                deleteProduct();
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

    public void addProduct() {
        DatabaseReader databaseReader = new DatabaseReader();
        String rawProduct = databaseReader.read(Constants.MESSAGE_FILE).toString();

        ProductParser productParser = new ProductParser();
        Produs product = productParser.parse(rawProduct);

        StringBuilder rawProducts = databaseReader.read(Constants.PRODUCTS_FILE);
        ArrayList<Produs> productsList = productParser.parseAll(rawProducts.toString());
        Products products = new Products(productsList);

        boolean result = false;
        if(product != null) {
            Produs existingProduct = products.findProduct(product.getId());

            if(existingProduct == null) {
                productsList.add(product);

                DatabaseWriter databaseWriter = new DatabaseWriter(Constants.PRODUCTS_FILE);
                result = databaseWriter.writeAll(productsList);
            }
        }

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(result));
    }

    public void listProductsCategory() {
        DatabaseReader databaseReader = new DatabaseReader();
        String category = databaseReader.read(Constants.MESSAGE_FILE).toString();

        StringBuilder rawProducts = databaseReader.read(Constants.PRODUCTS_FILE);
        ProductParser productParser = new ProductParser();
        ArrayList<Produs> productsList = productParser.parseAll(rawProducts.toString());

        ArrayList<Produs> categoryProducts = new ArrayList<>();
        for (Produs product : productsList) {
            if (product.getCategorie() == Categorie.valueOf(category)) {
                categoryProducts.add(product);
            }
        }

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.writeAll(categoryProducts);
    }

    public void deleteProduct() {
        DatabaseReader databaseReader = new DatabaseReader();

        int productId = Integer.parseInt(databaseReader.read(Constants.MESSAGE_FILE).toString());
        System.out.println(this.getClass() + ": received productId: " + productId);

        StringBuilder rawProducts = databaseReader.read(Constants.PRODUCTS_FILE);
        ProductParser productParser = new ProductParser();
        ArrayList<Produs> productList = productParser.parseAll(rawProducts.toString());

        boolean result = false;
        for (Produs product: productList) {
            if (product.getId() == productId) {
                result = productList.remove(product);
                break;
            }
        }

        if (result) {
            DatabaseWriter databaseWriter = new DatabaseWriter(Constants.PRODUCTS_FILE);
            result = databaseWriter.writeAll(productList);
        }

        DatabaseWriter output = new DatabaseWriter(Constants.OUTPUT_FILE);
        output.write(String.valueOf(result));
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
