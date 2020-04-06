package com.example.brodybeans2;

import java.util.ArrayList;

public class Order {
    private ArrayList<OrderItem> order;
    private String user;
    private Integer orderNumber;

    public Order() {}

    public Order(ArrayList<OrderItem> order, String user, Integer orderNumber) {
        this.order = order;
        this.user = user;
        this.orderNumber = orderNumber;
    }

    public ArrayList<OrderItem> getOrder() {
        return order;
    }

    public String getUser() {
        return user;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrder(ArrayList<OrderItem> order) {
        this.order = order;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
