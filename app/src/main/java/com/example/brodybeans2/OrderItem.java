package com.example.brodybeans2;

public class OrderItem {
    private String category;

    public OrderItem() {}

    public OrderItem(String item) {
        category = item;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

