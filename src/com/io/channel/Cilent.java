package com.io.channel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class FindMenu extends JFrame {

    private final JLayeredPane layeredPane;

    public FindMenu() {
        setLayout(new GridLayout(1, 1));

        layeredPane = new JLayeredPane() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon image = new ImageIcon("j_1.jpg");//导入图片
                image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_AREA_AVERAGING));//设置图片大小跟随面板大小
                g.drawImage(image.getImage(), 0, 0, this);//重新绘制面板
            }
        };
        //添加其他容器时，将其添加到不同的层，达到不遮盖其他容器的目的
        //layeredPane.add(JPanel, new Integer(200));
        add(layeredPane);
    }
}

public class Cilent {

    private JFrame frame;
    private JTextField textField;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Cilent window = new Cilent();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Cilent() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Login Interface");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);
        JLabel lblNewLabel = new JLabel("User Name :");
        lblNewLabel.setBounds(38, 43, 80, 34);
        frame.getContentPane().add(lblNewLabel);
        textField = new JTextField();
        textField.setBounds(155, 42, 227, 37);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        JLabel label = new JLabel("Password :");
        label.setBounds(38, 115, 80, 34);
        frame.getContentPane().add(label);
        passwordField = new JPasswordField();
        passwordField.setBounds(155, 115, 227, 37);
        frame.getContentPane().add(passwordField);
        JButton btnNewButton = new JButton("Register");
        btnNewButton.setBounds(60, 187, 115, 34);
        frame.getContentPane().add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                UserCheck UC=new UserCheck(textField.getText(),String.valueOf(passwordField.getPassword()));
                if (UC.getI() != 0)//有此用户
                {
                    frame.setVisible(false);
                } else {
                    textField.setText("");
                    passwordField.setText("");
                }
            }
        });
        JButton button = new JButton("Cancel");
        button.setBounds(242, 187, 115, 34);
        frame.getContentPane().add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                textField.setText("");
                passwordField.setText("");
            }
        });
    }
}

class UserCheck {
    private int i = 0;//用户级别:0不是用户、1是管理员、2是普通用户

    UserCheck(String name, String password) {
        String jdriver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String connectDB = "jdbc:odbc:Students";
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            Class.forName(jdriver);
            con = DriverManager.getConnection(connectDB);
            stmt = con.createStatement();
            String query = "select * from users where name='" + name + "' and passwd='" + password + "'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                //数据库中有此用户，访问成功
                i = Integer.parseInt(rs.getString(3));
                UsersCL UL = new UsersCL(i);
            } else {
                i = 0; //没有用户是默认是0级
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch  block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public int getI() {
        return i;
    }
}

class UsersCL implements ActionListener {
    private final JFrame frame;
    private final JTextField textField;
    private final JTextField textField_1;
    private final JTextField textField_2;
    private final JTextField textField_3;
    private int i = 0;
    private final JLabel label_3;
    private final JTextField textField_4;

    public UsersCL(int i) {
        this.i = i;
        frame = new JFrame();
        frame.setTitle("用户处理界面");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        JLabel lblNewLabel = new JLabel("学　号");
        lblNewLabel.setBounds(24, 32, 74, 29);
        frame.getContentPane().add(lblNewLabel);
        JLabel label = new JLabel("姓　名");
        label.setBounds(24, 71, 74, 29);
        frame.getContentPane().add(label);
        JLabel label_1 = new JLabel("年　龄");
        label_1.setBounds(24, 110, 74, 29);
        frame.getContentPane().add(label_1);
        label_3 = new JLabel("性　别");
        label_3.setBounds(24, 149, 74, 29);
        frame.getContentPane().add(label_3);
        JLabel label_2 = new JLabel("状　态");
        label_2.setBounds(24, 195, 74, 29);
        frame.getContentPane().add(label_2);
        textField = new JTextField();
        textField.setBounds(101, 34, 113, 25);
        frame.getContentPane().add(textField);
        textField.setColumns(10);
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(101, 73, 113, 25);
        frame.getContentPane().add(textField_1);
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(101, 112, 113, 25);
        frame.getContentPane().add(textField_2);
        textField_3 = new JTextField();
        textField_3.setEditable(false);
        textField_3.setColumns(10);
        textField_3.setBounds(101, 199, 288, 25);
        frame.getContentPane().add(textField_3);
        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(101, 149, 113, 25);
        frame.getContentPane().add(textField_4);
        if (1 == i) {
            JButton btnNewButton = new JButton("追　加");
            btnNewButton.setBounds(276, 41, 113, 29);
            frame.getContentPane().add(btnNewButton);
            btnNewButton.addActionListener(this);
            btnNewButton.setActionCommand("追加");
            JButton button_1 = new JButton("删　除");
            button_1.setBounds(276, 145, 113, 29);
            frame.getContentPane().add(button_1);
            button_1.addActionListener(this);
            button_1.setActionCommand("删除");
        }
        JButton button = new JButton("查　询");
        button.setBounds(276, 91, 113, 29);
        frame.getContentPane().add(button);
        button.addActionListener(this);
        button.setActionCommand("查询");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String name, age, sex, query = null;
        int num, age1, count = 0;
        num = Integer.parseInt(textField.getText());
        name = textField_1.getText();
        age1 = Integer.parseInt(textField_2.getText());
        sex = textField_4.getText();
        if (e.getActionCommand().equals("追加")) {
            query = "insert into students values(" + num + "," + "'" + name + "'," + age1 + ",'" + sex + "');";
            count = 1;
        } else if (e.getActionCommand().equals("查询")) {
            query = "select * from students where XSB=" + num + ";";
            count = 2;
        } else if (e.getActionCommand().equals("删除")) {
            query = "delete from students where XSB=" + num + " and name=" + "'" + name + "'";
            count = 3;
        }
        Statement stmt = null;
        ResultSet rs = null;
        Connection con = null;
        String jdriver = "sun.jdbc.odbc.JdbcOdbcDriver";
        String connectDB = "jdbc:odbc:Students";
        String query1 = null;
        try {
            Class.forName(jdriver);
            con = DriverManager.getConnection(connectDB);
            stmt = con.createStatement();
            if (count == 1) {
                query1 = "select * from students where XSB=" + num + ";";
                rs = stmt.executeQuery(query1);
            } else if (2 == count) {
                stmt.executeQuery(query);
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    textField_3.setText("已查找到此记录！");
                } else {
                    textField_3.setText("没有此记录，可以追加！");
                }
            } else if (3 == count) {
                query1 = "select * from students where XSB=" + num + " and name=" + "'" + name + "'";
                rs = stmt.executeQuery(query1);
                if (rs.next()) {
                    stmt.executeUpdate(query);
                    textField_3.setText("已删除此记录！");
                } else {
                    textField_3.setText("无此记录！");
                }
            }
        } catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            //关闭资源
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e2) {
                    // TODO : handle exception
                }
                stmt = null;
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e2) {
                    // TODO : handle exception
                }
                con = null;
            }
        }
    }
}