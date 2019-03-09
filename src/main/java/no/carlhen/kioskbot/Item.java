package no.carlhen.kioskbot;

public class Item{
    public String id;
    public String name;
    public String description;
    public Double price;
    public Boolean enabled;

    public Item(String id, String name, String description, Double price, Boolean enabled){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.enabled = enabled;
    }

    public boolean sameItem(Item item){
        boolean same = (
                id.equals(item.id) &&
                name.equals(item.name) &&
                description.equals(item.description) &&
                price.equals(item.price)
        );
        return same;
    }

    @Override
    public String toString(){
        return this.name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}