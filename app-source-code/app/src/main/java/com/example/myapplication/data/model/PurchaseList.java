package com.example.myapplication.data.model;

import java.util.List;

public class PurchaseList {
    public List<Purchase> purchases;
    public static class Purchase{
        public String  date;
        public String name;
        int id;

        public Purchase(String name, String date, int id){
            this.date = date;
            this.name = name;
            this.id = id;

        }
    }
}
