import java.time.LocalDateTime;

public class Items {

    private int id, code;
    private String title, producer;
    private LocalDateTime dateOfLastUpdate;

    public Items() {
    }

    public Items(int id, String title, int code,
                 String producer, LocalDateTime dateOfLastUpdate) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.producer = producer;
        this.dateOfLastUpdate = dateOfLastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public LocalDateTime getDateOfLastUpdate() {
        return dateOfLastUpdate;
    }

    public void setDateOfLastUpdate(LocalDateTime dateOfLastUpdate) {
        this.dateOfLastUpdate = dateOfLastUpdate;
    }

    @Override
    public String toString() {
        return "\nTitle= " + getTitle() + "  Code= "
                + getCode() + "  Producer= " +
                getProducer() + "  DateOfLastUpdate= " +
                getDateOfLastUpdate();
    }
}

