package com.app.hipermarket;

public class Main {
    public static void main(String[] args) {
        if (args.length != 0) {

            String database = args[0];

            HiperMarket hiperMarket = new HiperMarket(database);

            boolean valid = hiperMarket.isValidConfiguration();
            if (valid == true) {
                hiperMarket.startApplication();
            }
            else {
                System.out.println("Error: Invalid configuration.");
            }
        }
        else  {
            System.out.println("Error: Missing application parameters.");
        }
    }
}
