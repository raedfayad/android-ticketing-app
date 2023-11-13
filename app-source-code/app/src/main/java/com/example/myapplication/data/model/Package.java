package com.example.myapplication.data.model;

import androidx.annotation.NonNull;

import java.util.List;

public class Package {
    public String description;
    public int id;
    public String name;
    public double price;
    public int enabled;


    public Package(String name, String description, int id, int enabled, double price) {
        this.name=name;
        this.description=description;
        this.id=id;
        this.enabled=enabled;
        this.price=price;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

