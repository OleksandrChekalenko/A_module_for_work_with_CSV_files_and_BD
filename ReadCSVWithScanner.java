import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Cagy on 8/7/2017.
 */
public class ReadCSVWithScanner {
    static ReadFiles rf = new ReadFiles();
    public static List readItemsWithScanner(String fileName) throws IOException {
        if (fileName.equals(null)) {
            System.out.println("No file!");
            return null;
        }

        List<Items> itemsList = new ArrayList<>();
        //Items items = new Items();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){

            String line;
            Scanner scanner = null;
            int index = 0, count = 0;


            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    count++;
                    continue;
                }
                Items itm = new Items();
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0)
                        itm.setId(Integer.parseInt(data));
                    if (index == 1)
                        itm.setTitle(data);
                    if (index == 2)
                        itm.setCode(Integer.parseInt(data));
                    if (index == 3)
                        itm.setProducer(data);
                    if (index == 4) {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                        data = formatStringToDate(data);
                        LocalDateTime localDateTime = LocalDateTime.parse(data, fmt );
                        itm.setDateOfLastUpdate(localDateTime);
                    }
                    index++;
                }
                index = 0;
                itemsList.add(itm);
            }
            reader.close();
            //System.out.println(itemsList);

        } catch (IOException e) {
            System.out.println("Something wrong" + e.getMessage());
        }
        return itemsList;
    }

    public static List readCusromersWithScanner(String fileName) throws IOException {

        if (fileName.equals(null)) {
            System.out.println("No file!");
            return null;
        }
            List<Customers> customersList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            String line;
            Scanner scanner = null;
            Pattern pattern;
            int index = 0, count = 0;


            while ((line = reader.readLine()) != null) {
                if (count == 0) {
                    count++;
                    continue;
                }

                Customers cus = new Customers();
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                while (scanner.hasNext()) {
                    String data = scanner.next();
                    if (index == 0)
                        cus.setName(data);
                    if (index == 1)
                        cus.setDateOfBirth(data);
                    if (index == 2)
                        cus.setAddress(data);
                    if (index == 3)
                        cus.setGender(data);
                    if (index == 4)
                        cus.setPhoneNumber(data);
                    if (index == 5) {
                        List<Items> items = readItemsWithScanner("items.csv");
                        String res = data.replaceAll("\"", "");
                        data = res;
                        pattern = Pattern.compile(",");
                        String[] lastPurchaseArray = pattern.split(data);
                        List<Items> lastPurchase = new ArrayList<>();
                        for (int i = 0; i < items.size() ; i++) {
                            for (int j = 0; j <lastPurchaseArray.length ; j++) {
                                if (items.get(i).getId() == Integer.parseInt(lastPurchaseArray[j])) {
                                    lastPurchase.add(items.get(i));
                                }
                            }

                        }
                        cus.setLastPurchases(lastPurchase);
                       /* int[] numArr = Arrays.stream(data.split(",")).mapToInt(Integer::parseInt).toArray();
                        List<int[]> lastPurchase = new ArrayList<>();
                        lastPurchase.add(numArr);
                        cus.setLastPurchases(lastPurchase.get(0)[1]);*/

                    }
                    if (index == 6) {
                        pattern = Pattern.compile("/");
                        String[] dateOfLastPurchase = pattern.split(data);
                        cus.setDateOfLastPurchase(LocalDate.of(Integer.parseInt(dateOfLastPurchase[2]),
                                Month.of(Integer.parseInt(dateOfLastPurchase[0])),
                                Integer.parseInt(dateOfLastPurchase[1])));
                    }

                    index++;
                }
                index = 0;
                customersList.add(cus);
            }

            reader.close();
           System.out.println(customersList);


        } catch (IOException e) {
            System.out.println("Something wrong" + e.getMessage());
        }
        return customersList;
    }

    public static void setIdForCustomers(List<Customers> customers) throws IOException, SQLException {
        Connection con = rf.getConnection();
        if (customers == null){
            System.out.println("No file!");
            return;
        }

        for (int i = 0; i<customers.size(); i++){
            customers.get(i).setId(getIdCustomerFromBDByName(customers.get(i).getName()));
        }
    }

    public static int getIdCustomerFromBDByName(String name) throws IOException, SQLException {
        Connection con = rf.getConnection();
        int id = 0;
        ResultSet rs = null;
        String sqlCom = "SELECT id from customers where name=?";
        try (PreparedStatement statement = con.prepareStatement(sqlCom)){
            statement.setString(1, name);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
    public static List<Integer> getIdByNameFromBD(String name, Connection con){
        ResultSet rs = null;
        String comand = "Select id from " + name;
        try (PreparedStatement statement = con.prepareStatement(comand)) {
            rs = statement.executeQuery();
            List<Integer> id = new ArrayList<>();
            while (rs.next()){
                id.add(rs.getInt("id"));
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String formatStringToDate(String data){
        char[] lineCharArray = data.toCharArray();
        String dateTime = "";
        for (int i = 0; i<lineCharArray.length;i++) {
            if (lineCharArray[i] == ' ' && lineCharArray[i+2] == ':'){
                dateTime += lineCharArray[i];
                dateTime += '0';
            } else {
                dateTime += lineCharArray[i];
            }
        }
        return dateTime;
    }
}

