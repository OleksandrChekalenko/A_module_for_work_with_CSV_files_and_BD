import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class AnalyticalSamples {

    public AnalyticalSamples() throws IOException, SQLException {
    }

    public static Map<String, Integer> getMapItemsForGenderPurchases(String gender, Connection con) {
        Map<String, Integer> map = new HashMap<>();
        ResultSet rs = null;
        String mapForGender = "select i.TITLE, COUNT(*) AS quantity_purchases from PURCHASE p\n" +
                "                left outer join CUSTOMERS c on c.id=p.CUSTOMER_ID\n" +
                "                left outer join ITEMS i on i.id=p.ITEM_ID\n" +
                "                where c.gender=? GROUP BY i.TITLE ";
        try (PreparedStatement statement = con.prepareStatement(mapForGender)) {
            statement.setString(1, gender);
            rs = statement.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("title"), rs.getInt("quantity_purchases"));
            }
            return map;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Integer> getMapItemPurchasesForWeak(LocalDate from, LocalDate to, Connection con) {
        Map<String, Integer> mapMostPopularPurchasesForAWeek = new HashMap<>();
        ResultSet rs = null;
        String mostPopularPurchasesForAWeek = "select p.ITEM_ID, i.title, count(*) as quantity_purchases from PURCHASE p " +
                "left outer join customers c on c.ID=p.CUSTOMER_ID " +
                "left outer join ITEMS i on i.id=p.ITEM_ID " +
                "where p.DATEOFLASTPURCHASE>? and p.DATEOFLASTPURCHASE<? " +
                "group by i.TITLE, p.ITEM_ID ORDER BY quantity_purchases DESC";
        try (PreparedStatement st = con.prepareStatement(mostPopularPurchasesForAWeek)) {
            st.setDate(1, new Date(0000 - 00 - 00).valueOf(from));
            st.setDate(2, new Date(0000 - 00 - 00).valueOf(to));
            rs = st.executeQuery();
            while (rs.next()) {
                mapMostPopularPurchasesForAWeek.put(rs.getString("title"), rs.getInt("quantity_purchases"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapMostPopularPurchasesForAWeek;
    }

    public static void addColumn(String tableName, String columnName, Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String addColumnPrimary = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " VARCHAR(60)";
        try (Statement st = con.createStatement()) {
            st.execute(addColumnPrimary);

        } catch (SQLException e) {
            con.rollback();
            dropColumn(tableName, columnName, con);
            addColumn(tableName, columnName, con);
            System.out.println("Column " + columnName + " in table " + tableName + " already exist.\n Dropped column "
                    + columnName + " in table " + tableName + "\n Add column " + columnName);

        }
    }

    public static void dropColumn(String tableName, String columnName, Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String dropColumn = "ALTER TABLE " + tableName + " DROP COLUMN " + columnName;
        try (Statement st = con.createStatement()) {
            st.execute(dropColumn);
        } catch (SQLException e) {
            System.out.println("Cant drop column " + columnName + " in table " + tableName);
        }
    }

    public static Map<String, Integer> mostPopular(Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        int count = 0;
        Map<String, Integer> mostPopular = new HashMap<>();
        ResultSet rs = null;
        String mPopularSQL = "select i.title, count(*) as quantity_purchases from PURCHASE p\n" +
                "                left outer join customers c on c.ID=p.CUSTOMER_ID\n" +
                "                left outer join ITEMS i on i.id=p.ITEM_ID\n" +
                "                 group by i.TITLE ORDER BY quantity_purchases DESC";
        try (PreparedStatement st = con.prepareStatement(mPopularSQL)) {
            rs = st.executeQuery();
            while (rs.next()) {
                if (count < 3) {
                    mostPopular.put(rs.getString("title"), rs.getInt("quantity_purchases"));
                }
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostPopular;
    }

    public static Map<String, Integer> leastPopular(Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        int count = 0;
        Map<String, Integer> leastPopular = new HashMap<>();
        ResultSet rs = null;
        String lPopularSQL = "select i.title, count(*) as quantity_purchases from PURCHASE p\n" +
                "left outer join customers c on c.ID=p.CUSTOMER_ID\n" +
                "left outer join ITEMS i on i.id=p.ITEM_ID\n" +
                "group by i.TITLE ORDER BY quantity_purchases";
        try (PreparedStatement st = con.prepareStatement(lPopularSQL)) {
            rs = st.executeQuery();
            while (rs.next()) {
                if (count < 4) {
                    leastPopular.put(rs.getString("title"), rs.getInt("quantity_purchases"));
                }
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leastPopular;
    }

    public static void setMostPopular(Map<String, Integer> mostPopularMap, Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String sqlCom = "UPDATE ITEMS SET PRIMARYITEM = 'most popular' WHERE TITLE = ?";
        try (PreparedStatement st = con.prepareStatement(sqlCom)) {
            for (Map.Entry<String, Integer> entry : mostPopularMap.entrySet()) {
                String item = entry.getKey();
                st.setString(1, item);
                st.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Can`t UPDATE!");

        }
    }

    public static void setLeastPopular(Map<String, Integer> leastPopularMap, Connection con) throws IOException, SQLException {
        if (con == null) {
            con = ReadWriteCreateFilesNBD.getConnection();
        }
        String sqlCom = "UPDATE ITEMS SET CANDIDATETOREMOVE = 'least popular' WHERE TITLE = ?";
        try (PreparedStatement st = con.prepareStatement(sqlCom)) {
            for (Map.Entry<String, Integer> entry : leastPopularMap.entrySet()) {
                String item = entry.getKey();
                st.setString(1, item);
                st.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Can`t UPDATE!");

        }
    }

    public static void writeToCSV(Map<String, Integer> map, String fileName) throws IOException, ClassNotFoundException {
       String what;
        File f = new File(fileName);
        if (f.exists())
            f.delete();
        if (fileName.equals("PrimaryItems.csv"))
            what = " most popular;";
        else what = "least popular;";
        StringBuilder builder = new StringBuilder();
        builder.append("title;quantity_purchases;what;\n");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append(";");
            builder.append(entry.getValue());
            builder.append(";" + what + "\r\n");
        }
        String content = builder.toString().trim();
        //System.out.println(content);
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
        pw.println(content);
        pw.close();
/*String filename = "mytest.csv";
String entry = "Задача" +";"+"первая";
try {
    FileOutputStream out = openFileOutput(filename, Context.MODE_APPEND);
    out.write(entry.getBytes());
    out.close();

} catch (Exception e) {
    e.printStackTrace();
}*/
    }
}
