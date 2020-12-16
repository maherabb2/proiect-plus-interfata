package com.app.hipermarket.menu;

import com.app.hipermarket.parser.CashierParser;
import com.app.hipermarket.parser.SaleParser;
import com.app.hipermarket.products.Vanzare;
import com.app.hipermarket.readers.DatabaseReader;
import com.app.hipermarket.readers.DatabaseWriter;
import com.app.hipermarket.Constants;
import com.app.hipermarket.persoane.*;
import com.app.hipermarket.persoane.Cashiers;

import java.util.ArrayList;

public class AdminMenu extends Menu {
    private final String title;

    public AdminMenu (String title) {
        super();
        this.title = title;
    }

    @Override
    public void display() {
        System.out.println(formatHeader());
        System.out.println(formatTilte(title));
        System.out.println(formatHeader());

        System.out.println("1 - Adauga casier");
        System.out.println("2 - Sterge casier");
        System.out.println("3 - Afisare casieri");
        System.out.println("4 - Total vanzari");
        System.out.println("0 - Inapoi la meniul principal");
    }

    public Menu interpretCommand(char c) {
        menu = this;

        switch (c) {
            case '1':
                addCashier();
                break;
            case '2':
                removeCashier();
                break;
            case '3':
                listCashiers();
                break;
            case '4':
                sales();
                break;
            case '8':
                managerInquire();
                break;
            case '0':
                menu = new MainMenu();
                break;
            default:
                System.out.println("Invalid command");
        }

        return menu;
    }

    public void addCashier() {
        DatabaseReader databaseReader = new DatabaseReader();
        String rawCashier = databaseReader.read(Constants.MESSAGE_FILE).toString();

        CashierParser cashierParser = new CashierParser();
        Manager cashier = cashierParser.parse(rawCashier);

        StringBuilder rawCashiers = databaseReader.read(Constants.CASHIERS_FILE);
        ArrayList<Manager> cashierList = cashierParser.parseAll(rawCashiers.toString());
        Cashiers cashiers = new Cashiers(cashierList);

        boolean result = false;
        if (cashier != null) {
            Manager existingCashier = cashiers.findProduct(cashier.getUser());

            if (existingCashier == null) {
                cashierList.add(cashier);

                DatabaseWriter databaseWriter = new DatabaseWriter(Constants.CASHIERS_FILE);
                result = databaseWriter.writeAll(cashierList);
            }
        }

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(result));
    }

    public void removeCashier() {
        DatabaseReader databaseReader = new DatabaseReader();

        String username = databaseReader.read(Constants.MESSAGE_FILE).toString();
        System.out.println(this.getClass() + ": received username: " + username);

        StringBuilder rawCashiers = databaseReader.read(Constants.CASHIERS_FILE);
        CashierParser cashierParser = new CashierParser();
        ArrayList<Manager> cashierList = cashierParser.parseAll(rawCashiers.toString());

        boolean result = false;
        for (Manager cashier: cashierList) {
            if(cashier.getUser().equals(username)) {
                result = cashierList.remove(cashier);
                break;
            }
        }

        if (result) {
            DatabaseWriter databaseWriter = new DatabaseWriter(Constants.CASHIERS_FILE);
            result = databaseWriter.writeAll(cashierList);
        }

        DatabaseWriter output = new DatabaseWriter(Constants.OUTPUT_FILE);
        output.write(String.valueOf(result));
    }

    public void listCashiers() {
        DatabaseReader databaseReader = new DatabaseReader();
        StringBuilder rawCashiers = databaseReader.read(Constants.CASHIERS_FILE);
        CashierParser cashierParser = new CashierParser();
        ArrayList<Manager> cashierList = cashierParser.parseAll(rawCashiers.toString());

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.writeAll(cashierList);
    }

    public void sales() {
        DatabaseReader databaseReader = new DatabaseReader();
        StringBuilder rawSales =  databaseReader.read(Constants.SALES_FILE);

        SaleParser saleParser = new SaleParser();
        ArrayList<Vanzare> saleList = saleParser.parseAll(rawSales.toString());

        double profit = 0;
        for (Vanzare sale: saleList) {
            profit += sale.getSuma();
        }

        DatabaseWriter databaseWriter = new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(profit));
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
                returnValue = true;
            }
        }

        DatabaseWriter databaseWriter =  new DatabaseWriter(Constants.OUTPUT_FILE);
        databaseWriter.write(String.valueOf(returnValue));
    }
}
