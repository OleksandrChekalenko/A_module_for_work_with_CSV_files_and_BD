import java.io.IOException;
import java.sql.*;
import java.util.List;

public class WriteToBase {
    static ReadWriteCreateFilesNBD rf = new ReadWriteCreateFilesNBD();

    public static void writeItemsToBD(List<Items> items) throws IOException, SQLException {

        Connection con = rf.getConnection();
        if (items == null) {
            System.out.println("items = null");
            return;
        }
        String sqlCom = "insert into items (id,title, code, producer, dateOfLastUpdate) values (?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sqlCom)) {
            con.setAutoCommit(false);
            for (int i = 0; i < items.size(); i++) {
                if (ReadCSVWithScanner.getIdByNameFromBD("items", con).contains(items.get(i).getId())) {
                    continue;
                }
                st.setInt(1, items.get(i).getId());
                st.setString(2, items.get(i).getTitle());
                st.setInt(3, items.get(i).getCode());
                st.setString(4, items.get(i).getProducer());
                Timestamp timestamp = Timestamp.valueOf(items.get(i).getDateOfLastUpdate());
                st.setTimestamp(5, timestamp);
                st.executeUpdate();
            }
            con.commit();
            System.out.println("Items.csv was recorded to DB.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error! RollBack. Table ITEMS!" + "\n" + e.getMessage());
        }
    }

    public static void writeCustomersToBD(List<Customers> customers) throws IOException, SQLException {

        Connection con = rf.getConnection();
        if (customers == null) {
            System.out.println("No file!");
            return;
        }
        String sqlCom = "insert into CUSTOMERS (name,dateofbirth,address,gender,phonenumber,dateoflastpurchase) values (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sqlCom)) {
            con.setAutoCommit(false);
            for (int i = 0; i < customers.size(); i++) {
                st.setString(1, customers.get(i).getName());
                st.setString(2, customers.get(i).getDateOfBirth());
                st.setString(3, customers.get(i).getAddress());
                st.setString(4, customers.get(i).getGender());
                st.setString(5, customers.get(i).getPhoneNumber());
                st.setString(6, String.valueOf(customers.get(i).getDateOfLastPurchase()));
                st.executeUpdate();
            }
            con.commit();
            System.out.println("Customers.csv was recorded to DB.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error! RollBack. Table CUSTOMERS!" + "\n" + e.getMessage());
        }
    }

    public static void writePurchaseToBD(List<Customers> customers) throws IOException, SQLException {
        Connection con = rf.getConnection();
        if (customers == null) {
            System.out.println("No file!");
            return;
        }
        String sqlCom = "insert into purchase (DATEOFLASTPURCHASE, CUSTOMER_ID, ITEM_ID) values (?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sqlCom)) {
            con.setAutoCommit(false);
            for (int i = 0; i < customers.size(); i++) {
                for (int j = 0; j < customers.get(i).getLastPurchases().size(); j++) {
                    st.setDate(1, new Date(0000 - 00 - 00).valueOf(customers.get(i).getDateOfLastPurchase()));
                    st.setInt(2, customers.get(i).getId());
                    st.setInt(3, customers.get(i).getLastPurchases().get(j).getId());
                    st.executeUpdate();
                }
            }
            con.commit();
            System.out.println("Table PURCHASE was recorded to DB.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Error! RollBack. Table PURCHASE!" + "\n" + e.getMessage());
        }
    }
}



