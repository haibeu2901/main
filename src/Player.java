public class Player {
    private String id;
    private String realName;
    private int shirtNumber;
    private String shirtName;
    private String size;
    private String note;

    public Player(String id, String realName, int shirtNumber, String shirtName, String size, String note) {
        this.id = id;
        this.realName = realName;
        this.shirtNumber = shirtNumber;
        this.shirtName = shirtName;
        this.size = size;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public int getShirtNumber() {
        return shirtNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public void setShirtName(String shirtName) {
        this.shirtName = shirtName;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return id + "," + realName + "," + shirtNumber + "," + shirtName + "," + size + "," + note;
    }
}
