package com.app.bind;

import com.app.hipermarket.parser.CashierParser;
import com.app.hipermarket.readers.DatabaseReader;
import com.app.hipermarket.readers.DatabaseWriter;
import com.app.hipermarket.Constants;
import com.app.hipermarket.persoane.*;
import com.app.hipermarket.parser.ProductParser;
import com.app.hipermarket.products.Produs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class App {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField produs;
    private JButton scaneazaButton;
    private JTable shoppingTable;
    private JPanel client;
    private JPanel manager;
    private JPanel admin;
    private JButton totalPlataButton;
    private JButton finalizarePlataButton;
    private JLabel totalPlata;
    private JButton stergeButton;
    private JButton anulareButton;
    private JButton adaugaProdusButton;
    private JButton afisareCategorieButton;
    private JButton stergeProdusButton;
    private JTable printCategoryTable;
    private JButton adaugaCasierButton;
    private JButton afisareCasieriButton;
    private JButton stergeCasierButton;
    private JTable cashierTable;
    private JButton totalVanzariButton;
    private JLabel totalVanzariField;
    private JTextField quantity;

    private final DatabaseWriter inputFile;
    private final DatabaseWriter messageFile;
    private final DatabaseReader outputFile;

    public App() {
        inputFile = new DatabaseWriter(Constants.INPUT_FILE);
        messageFile = new DatabaseWriter(Constants.MESSAGE_FILE);
        outputFile = new DatabaseReader();

        DefaultTableModel model = new DefaultTableModel();
        for (int i = 1; i <= 6; i++) {
            model.addColumn("Col" + i);
        }
        model.addRow(new Object[]{"ID", "Nume", "Pret", "Cantitate", "Unitate", "Categorie"});
        shoppingTable.setModel(model);

        client.setName(Constants.CLIENT_NAME);
        manager.setName(Constants.CASHIER_NAME);
        admin.setName(Constants.ADMIN_NAME);

        inputFile.allowAccess(false);
        inputFile.write(Constants.CLIENT_MENU);
        inputFile.allowAccess(true);

        tabbedPane1.addChangeListener(new ChangeListener()  {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(this.getClass() + ": Switching tabs...");

                JPanel panel = (JPanel)tabbedPane1.getSelectedComponent();
                switch (panel.getName()) {
                    case Constants.CLIENT_NAME:
                        try {
                            inputFile.allowAccess(false);
                            inputFile.write(Constants.RETURN_MAIN_MENU);
                            inputFile.allowAccess(true);
                            Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                            inputFile.allowAccess(false);
                            inputFile.write(Constants.CLIENT_MENU);
                            inputFile.allowAccess(true);

                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case Constants.CASHIER_NAME:
                        if (managerAuthenticationFailed()) {
                          tabbedPane1.setSelectedIndex(0);
                          JFrame frame = new JFrame();
                          JOptionPane.showMessageDialog(frame, "Autentificare esuata!");
                        } else {
                            try {
                                inputFile.allowAccess(false);
                                inputFile.write(Constants.RETURN_MAIN_MENU);
                                inputFile.allowAccess(true);
                                Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                                inputFile.allowAccess(false);
                                inputFile.write(Constants.CASHIER_MENU);
                                inputFile.allowAccess(true);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case Constants.ADMIN_NAME:
                        if (adminAuthenticationFailed()) {
                            tabbedPane1.setSelectedIndex(0);
                            JFrame frame = new JFrame();
                            JOptionPane.showMessageDialog(frame, "Autentificare esuata!");
                        }
                        else {
                            try {
                                inputFile.allowAccess(false);
                                inputFile.write(Constants.RETURN_MAIN_MENU);
                                inputFile.allowAccess(true);
                                Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                                inputFile.allowAccess(false);
                                inputFile.write(Constants.ADMIN_MENU);
                                inputFile.allowAccess(true);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    default:
                        System.out.println(this.getClass() + ": Unimplemened menu on switching tabs!");
                }

            }
        });

        scaneazaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called scanare...");

                if (produs.getText().isEmpty()) {
                    String message = "Campul 'Produs Id' este gol.\nVa rugam introduceti o valoare.";
                    showMessageDialog(message);
                    return;
                }

                if (quantity.getText().isEmpty()) {
                    quantity.setText("1");
                }

                String rawData;
                try {
                    // Write message first because we don't want the client to read data too soon.
                    messageFile.write(produs.getText()+";"+quantity.getText());

                    inputFile.allowAccess(false);
                    inputFile.write(Constants.CLIENT_SCAN_PRODUCT);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    rawData = outputFile.read(Constants.OUTPUT_FILE).toString();
                    System.out.println(this.getClass() + ": Value of the raw data is: " + rawData);

                    if (rawData != null && !rawData.isEmpty() && !rawData.equals(" ")) {
                        ProductParser productParser = new ProductParser();
                        Produs product = productParser.parse(rawData);
                        if (Double.parseDouble(quantity.getText()) != 0) {
                            product.setQuantity(Double.parseDouble(quantity.getText()));
                        }
                        model.addRow(product.toArray());

                    }
                    else {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame, "Produsul nu a fost gasit!");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                produs.setText("");
                quantity.setText("");
                if (model.getRowCount() > 1) {
                    setTabbedPanes(false);
                }
                else {
                    setTabbedPanes(true);
                }
            }
        });

        totalPlataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called total de plata...");

                try {
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.TOTAL_COST);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    String value = outputFile.read(Constants.OUTPUT_FILE).toString();

                    if (value != null && !value.isEmpty() && !value.equals(" ")) {
                        totalPlata.setText(value);
                    } else {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame, "Total de plata nu a putut fi citit!");
                    }
                } catch (InterruptedException f) {
                    f.printStackTrace();
                }
            }
        });

        finalizarePlataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called finalizare plata...");

                if (model.getRowCount() <= 1) {
                    showMessageDialog("Cosul este gol!");
                    return;
                }

                JFrame frame = new JFrame();
                inputFile.allowAccess(false);
                inputFile.write(Constants.TOTAL_COST);
                inputFile.allowAccess(true);

                String paymentRemainder = "";
                while (true){
                    try {
                        Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                        paymentRemainder = outputFile.read(Constants.OUTPUT_FILE).toString();
                    } catch (InterruptedException f) {
                        f.printStackTrace();
                    }

                    if (Double.parseDouble(paymentRemainder) <= 0) {
                        break;
                    }

                    String message = "Total de plata " + paymentRemainder + " \n Introduceti suma:";
                    String amountToPay = JOptionPane.showInputDialog(frame, message);

                    try {
                        messageFile.write(amountToPay);
                        Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                        inputFile.allowAccess(false);
                        inputFile.write(Constants.FINAL_PAYMENT);
                        inputFile.allowAccess(true);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                JOptionPane.showMessageDialog(frame, "Plata efectuata cu success!");

                for (int i = model.getRowCount() - 1; i>=1; i--) {
                    model.removeRow(i);
                }
                totalPlata.setText("?");
                produs.setText("");
                quantity.setText("");
                setTabbedPanes(true);
            }
        });

        stergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called sterge produs...");

                if (model.getRowCount() <= 1) {
                    showMessageDialog("Cosul este gol!");
                    return;
                }

                if(managerAuthenticationFailed()) {
                    return;
                }

                JFrame frame = new JFrame();
                String productId = JOptionPane.showInputDialog(frame, "Introduceti un id produs");

                try {
                    messageFile.write(productId);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.REMOVE_PRODUCT);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    String productRemoved = outputFile.read(Constants.OUTPUT_FILE).toString();

                    if (Boolean.parseBoolean(productRemoved)) {
                        for (int i = 0; i < model.getRowCount(); i++) {
                            String element = model.getValueAt(i, 0).toString();

                            if (productId.equals(element)) {
                                model.removeRow(i);
                                break;
                            }
                        }
                    }
                } catch (InterruptedException g) {
                    g.printStackTrace();
                }

                if (model.getRowCount() <= 1) {
                    setTabbedPanes(true);
                }
            }
        });

        anulareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called anulare cos...");

                if (model.getRowCount() <= 1) {
                    showMessageDialog("Cosul este gol!");
                    return;
                }

                if(managerAuthenticationFailed()) {
                    return;
                }

                try {
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.CANCEL_ORDER);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    boolean removed = Boolean.parseBoolean(outputFile.read(Constants.OUTPUT_FILE).toString());
                    if (removed) {
                        for (int i = model.getRowCount() - 1; i >= 1; i--) {
                            model.removeRow(i);
                        }

                        produs.setText("");
                        quantity.setText("");
                        totalPlata.setText("?");
                        setTabbedPanes(true);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        afisareCategorieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called afisare categorie...");

                DefaultTableModel model = new DefaultTableModel();
                for (int i = 1; i <= 6; i++) {
                    model.addColumn("Col" + i);
                }
                model.addRow(new Object[]{"ID", "Nume", "Pret", "Cantitate", "Unitate", "Categorie"});
                printCategoryTable.setModel(model);

                JFrame frame = new JFrame();
                String category = JOptionPane.showInputDialog(frame, "Introduceti o categorie").toUpperCase();

                try {
                    messageFile.write(category);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.DISPLAY_CATEGORY);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND*2);
                    StringBuilder rawData = outputFile.read(Constants.OUTPUT_FILE);

                    if (rawData != null && !rawData.toString().isEmpty() && ! rawData.toString().contains(" ")) {
                        ProductParser productParser = new ProductParser();
                        ArrayList<Produs> products = productParser.parseAll(rawData.toString());

                        for (Produs product: products) {
                            model.addRow(product.toArray());
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Nu exista produse in categoria "+ category.toUpperCase());
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        stergeProdusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called sterge categorie...");

                JFrame frame = new JFrame();
                String productId = JOptionPane.showInputDialog(frame, "Introduceti un id");

                try {
                    messageFile.write(productId);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.REMOVE_ITEM_ID);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                    String rawData = outputFile.read(Constants.OUTPUT_FILE).toString();

                    if (rawData != null && !rawData.isEmpty() && !rawData.equals(" ")) {
                        boolean success = Boolean.parseBoolean(rawData);

                        if (success) {
                            JOptionPane.showMessageDialog(frame, "Produsul " + productId + " a fost sters cu success!");
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Produsul " + productId + " nu a fost sters!");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Raspunsul nu a putut fi citit!");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        adaugaProdusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] quantities = { "KG", "L", "Buc" };
                String[] categories = { "Fructe", "Legume", "Mezeluri", "Lactate", "Panificatie", "Curatenie", "Bauturi" };
                JComboBox<String> quantityCombo = new JComboBox<>(quantities);
                JComboBox<String> categoryCombo = new JComboBox<>(categories);
                JTextField idField = new JTextField("");
                JTextField nameField = new JTextField("");
                JTextField priceField = new JTextField("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Id:"));
                panel.add(idField);
                panel.add(new JLabel("Nume:"));
                panel.add(nameField);
                panel.add(new JLabel("Pret:"));
                panel.add(priceField);
                panel.add(new JLabel("Cantitate:"));
                panel.add(quantityCombo);
                panel.add(new JLabel("Categorie:"));
                panel.add(categoryCombo);


                int result = JOptionPane.showConfirmDialog(null, panel, "Adauga Produs",
                                                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {

                    String rawMessage = idField.getText() + ";" + nameField.getText() + ";" + priceField.getText() + ";" + "1" + ";" +
                                                        quantityCombo.getSelectedIndex() + ";" + categoryCombo.getSelectedIndex();
                    try {
                        messageFile.write(rawMessage);

                        Thread.sleep(Constants.SLEEP_HALF_SECOND);
                        inputFile.allowAccess(false);
                        inputFile.write(Constants.ADD_PRODUCT);
                        inputFile.allowAccess(true);

                        Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                        String response = outputFile.read(Constants.OUTPUT_FILE).toString();

                        if (response != null && !response.isEmpty() && !response.contains(" ")) {
                            if (Boolean.parseBoolean(response)) {
                                JFrame frame = new JFrame();
                                JOptionPane.showMessageDialog(frame, "Produsul " + nameField.getText() + " adaugat cu sucess!");
                            }
                            else {
                                JFrame frame = new JFrame();
                                JOptionPane.showMessageDialog(frame, "Produsul " + nameField.getText() + " nu a fost gasit sau deja exista!");
                            }
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println(this.getClass() + ": Cancelled adauga produs");
                }
            }
        });

        afisareCasieriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called afisare casieri...");

                DefaultTableModel model = new DefaultTableModel();
                for (int i = 1; i <= 2; i++) {
                    model.addColumn("Col" + i);
                }
                model.addRow(new Object[]{ "User", "Parola" });
                cashierTable.setModel(model);

                try {
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.DISPLAY_CASHIERS);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND*2);
                    StringBuilder rawData = outputFile.read(Constants.OUTPUT_FILE);

                    CashierParser cashierParser = new CashierParser();
                    ArrayList<Manager> cashierList = cashierParser.parseAll(rawData.toString());
                    for (Manager cashier : cashierList) {
                        model.addRow(cashier.toArray());
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        adaugaCasierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called adauga casier...");

                JTextField userField = new JTextField("");
                JTextField passField = new JTextField("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("User:"));
                panel.add(userField);
                panel.add(new JLabel("Parola:"));
                panel.add(passField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Adauga casier",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {

                    String rawMessage = userField.getText() + ";" + passField.getText();
                    try {
                        messageFile.write(rawMessage);

                        Thread.sleep(Constants.SLEEP_HALF_SECOND);
                        inputFile.allowAccess(false);
                        inputFile.write(Constants.ADD_CASHIER);
                        inputFile.allowAccess(true);

                        Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                        String response = outputFile.read(Constants.OUTPUT_FILE).toString();

                        if (response != null && !response.isEmpty() && !response.contains(" ")) {
                            if (Boolean.parseBoolean(response)) {
                                JFrame frame = new JFrame();
                                JOptionPane.showMessageDialog(frame, "Casierul " + userField.getText() + " adaugat cu sucess!");
                            }
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println(this.getClass() + ": Cancelled adauga casier");
                }
            }
        });

        stergeCasierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called sterge casier...");

                JFrame frame = new JFrame();
                String username = JOptionPane.showInputDialog(frame, "Introduceti nume casier");

                try {
                    messageFile.write(username);
                    Thread.sleep(Constants.SLEEP_HALF_SECOND);
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.DELETE_CASHIER);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                    String rawData = outputFile.read(Constants.OUTPUT_FILE).toString();

                    if (rawData != null && !rawData.isEmpty() && !rawData.equals(" ")) {
                        boolean success = Boolean.parseBoolean(rawData);

                        if (success) {
                            JOptionPane.showMessageDialog(frame, "Casierul " + username + " a fost sters cu success!");
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "casierul " + username + " nu a fost gasit!");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Raspunsul nu a putut fi citit!");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        totalVanzariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(this.getClass() + ": Called total vanzari...");

                try {
                    inputFile.allowAccess(false);
                    inputFile.write(Constants.TOTAL_PAYMENTS);
                    inputFile.allowAccess(true);

                    Thread.sleep(Constants.SLEEP_HALF_SECOND * 2);
                    String payments = outputFile.read(Constants.OUTPUT_FILE).toString();

                    if (payments != null && !payments.isEmpty() && !payments.equals(" ")) {
                        totalVanzariField.setText(payments);
                    }
                    else {
                        JFrame frame = new JFrame();
                        JOptionPane.showMessageDialog(frame, "Raspunsul nu a putut fi citit!");
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void showMessageDialog(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message);
    }

    private void setTabbedPanes(boolean status) {
        for (int i = 1; i < tabbedPane1.getTabCount(); i++) {
            tabbedPane1.setEnabledAt(i, status);
        }
    }

    private boolean managerAuthenticationFailed() {
        JFrame frame = new JFrame();
        String username = JOptionPane.showInputDialog(frame,"User casier");
        String password = JOptionPane.showInputDialog(frame,"Parola casier");

        boolean authStatus = false;
        try {
            messageFile.write(username+";"+password);

            Thread.sleep(Constants.SLEEP_HALF_SECOND);
            inputFile.allowAccess(false);
            inputFile.write(Constants.MANAGER_INQUIRE);
            inputFile.allowAccess(true);

            Thread.sleep(Constants.SLEEP_HALF_SECOND);
            String response = outputFile.read(Constants.OUTPUT_FILE).toString();

            if (response != null && !response.isEmpty() && !response.contains(" ")) {
                authStatus = Boolean.parseBoolean(response);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (authStatus) {
            System.out.println(this.getClass() + ": " + username + " authentication success");
        }
        else {
            System.out.println(this.getClass() + ": " + username + " authentication failure");
        }

        return !authStatus;
    }

    private boolean adminAuthenticationFailed() {
        JFrame frame = new JFrame();
        String username = JOptionPane.showInputDialog(frame,"User admin");
        String password = JOptionPane.showInputDialog(frame,"Parola admin");

        boolean authStatus = false;
        try {
            messageFile.write(username+";"+password);

            Thread.sleep(Constants.SLEEP_HALF_SECOND);
            inputFile.allowAccess(false);
            inputFile.write(Constants.ADMIN_INQUIRE);
            inputFile.allowAccess(true);

            Thread.sleep(Constants.SLEEP_HALF_SECOND);
            String response = outputFile.read(Constants.OUTPUT_FILE).toString();

            if (response != null && !response.isEmpty() && !response.contains(" ")) {
                authStatus = Boolean.parseBoolean(response);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (authStatus) {
            System.out.println(this.getClass() + ": " + username + " authentication success");
        }
        else {
            System.out.println(this.getClass() + ": " + username + " authentication failure");
        }

        return !authStatus;
    }

    public static void main(String[] args) {
        DatabaseReader reader = new DatabaseReader();
        String title = reader.read(Constants.CONFIG_FILE).toString();

        if(title == null || title.isEmpty() || title.charAt(0) == ' ') {
            JFrame option = new JFrame();
            JOptionPane.showMessageDialog(option, "Nu exista nici un nume in fisierul config.txt!",
                    "Eroare", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        JFrame frame = new JFrame( title);
        frame.setContentPane(new App().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
