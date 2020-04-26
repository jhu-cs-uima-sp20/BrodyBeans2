package com.example.brodybeans2;

import java.util.ArrayList;

public class Order {
    private ArrayList<OrderItem> order;
    private String email;
    private Integer orderNumber;
    private Boolean inProg;
    private String firebaseKey;

    public Order() {}

    public Order(ArrayList<OrderItem> order, String user, Integer orderNumber) {
        this.order = order;
        this.email = user;
        this.orderNumber = orderNumber;
        this.inProg = false;
        this.firebaseKey = "";
    }

    public ArrayList<OrderItem> getOrder() {
        return order;
    }

    public String getEmail() {
        return email;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public Boolean getProgressStatus() {
        return inProg;
    }

    public void setOrder(ArrayList<OrderItem> order) {
        this.order = order;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setEmail(String user) {
        this.email = user;
    }

    public void setProgressStatus(Boolean progress) {
        this.inProg = progress;
    }

    public String getFirebaseKey() { return firebaseKey; }

    public void setFirebaseKey(String fKey) { this.firebaseKey = fKey; }
}
