package com.example.myapplication.placeholder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PackageContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<PackageItem> ITEMS = new ArrayList<PackageItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, PackageItem> ITEM_MAP = new HashMap<String, PackageItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(PackageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PackageItem createPlaceholderItem(int position) {
        return new PackageItem(String.valueOf(position), "Event " + makeTime() + " " + makeCost(position), makeTime(), makeCost(position));
    }

//    private static String makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//    }

    private static String makeTime() {
        Instant now = Instant.now();
        return now.toString();
    }

    private static String makeCost(int position) {
        return Integer.toString(300+position);
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class PackageItem {
        public final String id;
        public final String name;
        public final String time;
        public final String cost;

        public PackageItem(String id, String name, String time, String cost) {
            this.id = id;
            this.name = name;
            this.time = time;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}