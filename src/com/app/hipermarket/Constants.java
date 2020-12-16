package com.app.hipermarket;

public class Constants {
    // FILES
    public final static String DATABASE = "database/";
    public final static String CONFIG_FILE = "config.txt";

    public static final String INPUT_FILE = "input.txt";
    public static final String OUTPUT_FILE = "output.txt";
    public static final String MESSAGE_FILE = "messages.txt";

    public static final String ADMINS_FILE = "admin.txt";
    public static final String CASHIERS_FILE = "casieri.txt";
    public final static String PRODUCTS_FILE = "produse.txt";
    public final static String SALES_FILE = "vanzari.txt";

    // GUI_STUFF
    public static final long SLEEP_HALF_SECOND = 500L;
    public static final String CLIENT_NAME = "client";
    public static final String CASHIER_NAME = "casier";
    public static final String ADMIN_NAME = "admin";

    // MAIN_MENU
    public static final String CLIENT_MENU = "c";
    public static final String CASHIER_MENU = "m";
    public static final String ADMIN_MENU = "a";

    // CLIENT_MENU
    public static final String RETURN_MAIN_MENU = "0";
    public static final String CLIENT_SCAN_PRODUCT = "1";
    public static final String FINAL_PAYMENT = "2";
    public static final String MANAGER_INQUIRE = "8";
    public static final String TOTAL_COST = "4";
    public static final String REMOVE_PRODUCT = "5";
    public static final String CANCEL_ORDER = "6";
    public static final String ADMIN_INQUIRE = "9";

    // MANAGER_MENU
    public static final String ADD_PRODUCT = "1";
    public static final String DISPLAY_CATEGORY = "2";
    public static final String REMOVE_ITEM_ID = "3";

    // ADMIN_MENU
    public static final String ADD_CASHIER = "1";
    public static final String DELETE_CASHIER = "2";
    public static final String DISPLAY_CASHIERS = "3";
    public static final String TOTAL_PAYMENTS = "4";
}
