import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ReadWriteCreateFilesNBD {

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

        } catch (IOException e) {
            throw new IOException(e);
        } catch (SQLException e) {
            System.out.println("Turn off DataBase! " + "\n" + e.getMessage());
            throw new SQLException(e);
        }
    }

    public static void start() throws IOException, SQLException, ClassNotFoundException {
        CreareDataBase bd = new CreareDataBase();
        bd.createBase(getConnection());

        ReadCSVWithScanner rs = new ReadCSVWithScanner();
        List<Customers> customersList = rs.readCustomersWithScanner("Customers.csv");
        List<Items> itemsList = rs.readItemsWithScanner("items.csv");


        WriteToBase writeToBase = new WriteToBase();
        writeToBase.writeCustomersToBD(customersList);
        writeToBase.writeItemsToBD(itemsList);
        rs.setIdForCustomers(customersList);

        writeToBase.writePurchaseToBD(customersList);
        AnalyticalSamples as = new AnalyticalSamples();

        Map<String, Integer> femaleMap = as.getMapItemsForGenderPurchases("female", getConnection());
        for (Map.Entry<String, Integer> entry : femaleMap.entrySet()) {
            String item = entry.getKey();
            Integer quantityPurchases = entry.getValue();
            System.out.println(item + " = " + quantityPurchases + " purchases.");
        }
        System.out.println();

        Map<String, Integer> mapMostPopularPurchasesForAWeek = as.getMapItemPurchasesForWeak(LocalDate.of(2017, 6, 20), LocalDate.of(2017, 6, 26), getConnection());
        for (Map.Entry<String, Integer> entry : mapMostPopularPurchasesForAWeek.entrySet()) {
            String item = entry.getKey();
            Integer purchases = entry.getValue();
            System.out.println(item + " =   " + purchases + " purchases.");
        }
        as.addColumn("ITEMS", "PrimaryItem", getConnection());
        as.addColumn("ITEMS", "CandidateToRemove", getConnection());

        Map<String, Integer> mostPopular = as.mostPopular(getConnection());
        for (Map.Entry<String, Integer> entry : mostPopular.entrySet()) {
            String item = entry.getKey();
            Integer purchases = entry.getValue();
            System.out.println(item + " =   " + purchases + " purchases.");
        }
        System.out.println("\t\t");
        Map<String, Integer> leastPopular = as.leastPopular(getConnection());
        for (Map.Entry<String, Integer> entry : leastPopular.entrySet()) {
            String item = entry.getKey();
            Integer purchases = entry.getValue();
            System.out.println(item + " =   " + purchases + " purchases.");
        }

        as.setMostPopular(mostPopular, getConnection());
        as.setLeastPopular(leastPopular, getConnection());

        as.writeToCSV(mostPopular, "PrimaryItems.csv");
        as.writeToCSV(leastPopular, "CandidateToRemove.csv");

    }
}
