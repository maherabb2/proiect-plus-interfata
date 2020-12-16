package com.app.hipermarket.menu;

public class MainMenu extends Menu {
    private String title;

    public MainMenu() {
        super();
        this.title = "";
    }

    @Override
    public void display() {
        System.out.println(formatHeader());
        System.out.println(formatTilte(title));
        System.out.println(formatHeader());

        System.out.println("c - Client");
        System.out.println("m - Manager");
        System.out.println("a - Admin");
        System.out.println("x - Iesire");
    }

    public Menu interpretCommand(char c) {
        switch (c) {
            case 'c':
                menu = new ClientMenu("Title - client");
                break;
            case 'm':
                menu = new ManagerMenu("Title - casier");
                break;
            case 'a':
                menu = new AdminMenu("Title - admin");
                break;
            case 'i':
                menu = new MainMenu();
                break;
            case 'x':
                System.out.println("Iesire din aplicatie");
                menu = null;
                break;
            default:
                System.out.println("Invalid command");
                menu = this;
        }

        return menu;
    }
}
