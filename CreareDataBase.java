import java.io.IOException;
import java.sql.*;

public class CreareDataBase {

    public static void createBase(Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String createTableCustomers = "CREATE TABLE CUSTOMERS\n" +
                "(\n" +
                "\tID INT NOT NULL GENERATED ALWAYS AS IDENTITY,\n" +
                "\tNAME VARCHAR(60) NOT NULL,\n" +
                "\tDateOfBirth VARCHAR(20) NOT NULL, \n" +
                "\tAddress VARCHAR(100) NOT NULL,\n" +
                "\tGender VARCHAR(20) NOT NULL,\n" +
                "\tPhoneNumber VARCHAR(20) NOT NULL,\n" +
                "\tDateOfLastPurchase VARCHAR(20) NOT NULL,\n" +
                "\tPRIMARY KEY(id)\n" +
                ")";
        String createTableItems = "CREATE TABLE ITEMS\n" +
                "(\n" +
                "\tid INT NOT NULL,\n" +
                "\ttitle VARCHAR(60) NOT NULL,\n" +
                "\tcode  INT NOT NULL,\n" +
                "\tproducer VARCHAR(60) NOT NULL,\n" +
                "\tdateOfLastUpdate TIMESTAMP NOT NULL,\n" +
                "\tPRIMARY KEY(id)\n" +
                ")";
        String createTablePurchase = "CREATE TABLE PURCHASE\n" +
                "(\n" +
                "DATEOFLASTPURCHASE VARCHAR(20) NOT NULL,\n" +
                "CUSTOMER_ID INT CONSTRAINT customer_fk REFERENCES CUSTOMERS,\n" +
                "ITEM_ID  INT CONSTRAINT items_fk REFERENCES ITEMS\n" +
                ")";
        try (Statement st = con.createStatement()) {
            con.setAutoCommit(false);
            st.execute(createTableCustomers);
            st.execute(createTableItems);
            st.execute(createTablePurchase);
            con.commit();
            System.out.println("DataBase was created.");
        } catch (SQLException e) {
            con.rollback();
            dropTables(con);
            createBase(con);
            //System.out.println("Error! Rollback! DataBase was`t created! \n Try to DROP TABLES. \n But omething went wrong(" + e.getMessage());

        }
    }

    public static void dropTables(Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String dropTablePurcase = "DROP TABLE PURCHASE";
        String dropTableCustomers = "DROP TABLE CUSTOMERS";
        String dropTableItems = "DROP TABLE ITEMS";
        try (Statement st = con.createStatement()) {
            con.setAutoCommit(false);
            st.execute(dropTablePurcase);
            st.execute(dropTableCustomers);
            st.execute(dropTableItems);
            con.commit();
            System.out.println("Tables was dropped.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Rollback! Tables was`t dropped. Try to create tables. " + e.getMessage());
        }
    }
}
