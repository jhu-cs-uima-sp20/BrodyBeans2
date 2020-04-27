package com.example.brodybeans2;

public class OrderItem {
    private String category;
    private String temperature;
    private String modifications;
    private String size;



    public OrderItem() {}

    public OrderItem(String item) {
        category = item;
    }

    public OrderItem(String item, String temp, String sz) {
        category = item;
        temperature = temp;
        size = sz;
    }

    public OrderItem(String item, String temp, String sz, String mod) {
        category = item;
        temperature = temp;
        size = sz;
        modifications = mod;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String sz) {
        this.size = sz;
    }
    
    public String getMod() {return this.modifications;}

    public void setMod(String mod) { this.modifications = mod; }

    public String getTemp() { return temperature;}

    public void setTemp(String temp) { this.temperature = temp; }

}
