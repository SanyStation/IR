package com.ak.ir.index;

/**
 * Created by sanystation on 01/11/15.
 */
public enum Zone {

    TITLE(0.2), BODY(0.5), AUTHOR(0.2), GENRE(0.05), KEYWORDS(0.05);

    private double weight;

    Zone(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

}
