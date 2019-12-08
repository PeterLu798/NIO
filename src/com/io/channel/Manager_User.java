package com.io.channel;

import javax.swing.*;

public class Manager_User{

    JFrame frame;
    JPanel panel;
    JComboBox identity;
    JComboBox click;

    Manager_User() {
        frame = new JFrame();

        frame.setSize(400, 250);
        frame.setLocationRelativeTo(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Box verticalBox = Box.createVerticalBox();

        identity = new JComboBox();
        identity.addItem("Manager");
        identity.addItem("User");
        click = new JComboBox();
        click.addItem("Register");
        click.addItem("Login");

        verticalBox.add(identity);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(click);

//        panel = new JPanel(new LayoutManager());
        panel.add(verticalBox);

        frame.add(panel);



        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Manager_User obj = new Manager_User();
    }
}