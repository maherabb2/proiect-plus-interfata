package com.app.bind;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddProduct {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JPanel panel1;
    private JButton OKButton;
    private JButton cancelButton;

    private JFrame frame = new JFrame( "Add product");

    public AddProduct() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("HERE");

//                frame.pack();
                frame.setVisible(false);
                frame.dispose();

            }
        });
    }

    public void createForm() {

        frame.setContentPane(new AddProduct().panel1);
//        frame.pack();
        frame.setVisible(true);
    }
}
