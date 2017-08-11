import java.time.LocalDate;
import java.util.List;

public class Customers {
    private int id;
    private String name, address, gender, phoneNumber, dateOfBirth;
    private LocalDate dateOfLastPurchase;
    private List<Items> lastPurchases;

    public Customers() {
    }

    public Customers(int id, String name, String address, String gender, String phoneNumber, String dateOfBirth, List<Items> lastPurchases, LocalDate dateOfLastPurchase) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.dateOfLastPurchase = dateOfLastPurchase;
        this.lastPurchases = lastPurchases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Items> getLastPurchases() {
        return lastPurchases;
    }

    public void setLastPurchases(List<Items> lastPurchases) {
        this.lastPurchases = lastPurchases;

    }

    public LocalDate getDateOfLastPurchase() {
        return dateOfLastPurchase;
    }

    public void setDateOfLastPurchase(LocalDate dateOfLastPurchase) {
        this.dateOfLastPurchase = dateOfLastPurchase;
    }


    @Override
    public String toString() {
        return "\nName= " + getName() + "  DateOfBirth=" + getDateOfBirth() +
                "  Address=" + getAddress() + "  Gender=" + getGender() +
                "  PhoneNumber=" + getPhoneNumber() + "  LastPurchases=" +
                getLastPurchases() + "  DateOfLastPurchase=" + getDateOfLastPurchase();
    }

}
