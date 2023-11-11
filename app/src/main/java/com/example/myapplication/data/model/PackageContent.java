package com.example.myapplication.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageContent {
    /**
     * An array of sample (placeholder) items.
     */
    public static final List<Package> ITEMS = new ArrayList<Package>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, Package> ITEM_MAP = new HashMap<String, Package>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(Package item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);
    }

    private static Package createPlaceholderItem(int position) {
        return new Package("Package: Raed" , "", position, 1, position);
    }
}
