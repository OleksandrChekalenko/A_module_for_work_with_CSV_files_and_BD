import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Created by Cagy on 8/7/2017.
 */
public class ReadFiles {

    public static Connection getConnection() throws IOException, SQLException {
        try {
            Connection conn = null;
            Properties props = new Properties();
            InputStreamReader in = new InputStreamReader(new FileInputStream("appProperties.txt"), "UTF-8");
            props.load(in);
            in.close();

            String connString = props.getProperty("DBConnectionString");
            conn = DriverManager.getConnection(connString);
            return conn;

        } catch (IOException e ) {
            throw new IOException(e);
        } catch (SQLException e) {
            System.out.println("Turn off DataBase! " + "\n" + e.getMessage());
            throw new SQLException(e);
        }
    }

    public static void main(String[] args) throws IOException, SQLException {
        ReadCSVWithScanner rs = new ReadCSVWithScanner();
       List<Customers> customersList = rs.readCusromersWithScanner("Customers.csv");
        List<Items> itemsList = rs.readItemsWithScanner("items.csv");


        WriteToBase writeToBase = new WriteToBase();
        writeToBase.writeCustomersToBD(customersList);
        writeToBase.writeItemsToBD(itemsList);
        rs.setIdForCustomers(customersList);

        writeToBase.writePurchaseToBD(customersList);


    }
}
