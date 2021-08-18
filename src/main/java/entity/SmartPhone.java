package entity;

public class SmartPhone {
    private String id;
    private String name;
    private String desciption;
    private String price;

    public SmartPhone(String id, String name, String desciption, String price) {
        this.id = id;
        this.name = name;
        this.desciption = desciption;
        this.price = price;
    }

    @Override
    public String toString() {
        return "SmartPhone{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desciption='" + desciption + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public SmartPhone() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
