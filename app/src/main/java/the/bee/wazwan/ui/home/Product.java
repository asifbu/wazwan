package the.bee.wazwan.ui.home;

public class Product {

    String Id;
    String Name;
    String Description;
    String Phone;

    public Product() {
    }

    public Product(String id, String name, String description, String phone) {
        Id = id;
        Name = name;
        Description = description;
        Phone = phone;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
