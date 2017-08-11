import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Cagy on 8/10/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, SQLException {
     try {
        ReadWriteCreateFilesNBD.start();
         System.out.println("ALL OK =)");
    } catch (Exception e){
         System.out.println("Problem with start program.\n" + e.getMessage());
     }
    }
}
