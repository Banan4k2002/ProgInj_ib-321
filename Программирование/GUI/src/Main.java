import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Currency{
    String name;
    float conversion;
    Currency(String name, float conversion){
        this.name = name;
        this.conversion = conversion;
    }
    public String getName() {
        return this.name;
    }
    public float getConversion() {
        return this.conversion;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
class Form implements ActionListener {
    static JFrame frame;
    static JPanel panel_1, panel_2, panel_3;
    static JLabel description, current, value, result_description, result;
    static JComboBox comboBox;
    static JTextField textField;
    static JButton button;

    public void create_form() {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Currency[] items = new Currency[3];
        try {
            // db parameters
            String url = "jdbc:sqlite:G:/Учеба/JAVA/GUI/src/db/Конвертатор.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            statement = conn.createStatement();
            resultSet = statement.executeQuery("Select Name, Conversion from Currency");
            int i = 0;
            while (resultSet.next())
            {
                items[i] = new  Currency(resultSet.getString("Name"), resultSet.getFloat("Conversion"));
                i++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

            frame = new JFrame();
            frame.setResizable(true);
            frame.setTitle("Конвертер");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new GridLayout(3, 1, 10, 10));
            panel_1 = new JPanel();
            panel_2 = new JPanel();
            panel_3 = new JPanel();
            frame.setVisible(true);

            panel_1.setLayout(new GridLayout(1, 1, 5, 5));
            description = new JLabel("Выберите валюту, введите сумму и нажмите на кнопку 'Пересчет'.", SwingConstants.CENTER);
            panel_1.add(description);
            frame.add(panel_1);

            panel_2.setLayout(new GridLayout(2, 2, 5, 5));
            current = new JLabel("Выберите валюту", SwingConstants.LEFT);
            value = new JLabel("Введте сумму", SwingConstants.LEFT);
            comboBox = new JComboBox(items);
            textField = new JTextField();
            panel_2.add(current);
            panel_2.add(comboBox);
            panel_2.add(value);
            panel_2.add(textField);
            frame.add(panel_2);

            panel_3.setLayout(new GridLayout(1, 3, 5, 5));
            result_description = new JLabel("Итог: ", SwingConstants.CENTER);
            result = new JLabel("", SwingConstants.LEFT);
            button = new JButton("Пересчет");
            button.addActionListener(this);
            panel_3.add(result_description);
            panel_3.add(result);
            panel_3.add(button);
            frame.add(panel_3);

            frame.pack();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            try {
                String text = textField.getText();
                float float_text = Float.parseFloat(text);
                Currency from_comboBox = (Currency) comboBox.getSelectedItem();
                float get_conversion = from_comboBox.getConversion();
                text = "" + float_text*get_conversion;
                result.setText(text);
            }
            catch (NumberFormatException nfe) {
                result.setText("Введите целое число");
            }
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Form a = new Form();
        a.create_form();
    }
}
