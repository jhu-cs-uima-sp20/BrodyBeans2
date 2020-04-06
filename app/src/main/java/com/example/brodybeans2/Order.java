package com.example.brodybeans2;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Order {
    private ArrayList<OrderItem> order;
    private FirebaseUser user;
    private int orderNumber;

    public Order(ArrayList<OrderItem> order, FirebaseUser user, int orderNumber) {
        this.order = order;
        this.user = user;
        this.orderNumber = orderNumber;
    }

    public ArrayList<OrderItem> getOrder() {
        return order;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrder(ArrayList<OrderItem> order) {
        this.order = order;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
