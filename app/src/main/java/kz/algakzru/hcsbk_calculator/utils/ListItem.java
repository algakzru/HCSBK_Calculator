package kz.algakzru.hcsbk_calculator.utils;

/**
 * Created by 816856 on 3/13/2018.
 */

public class ListItem {

    private String key;
    private double value;

    public ListItem(String key, double value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public double getValue() {
        return value;
    }
}
