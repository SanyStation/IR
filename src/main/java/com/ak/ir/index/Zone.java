package com.ak.ir.index;

/**
 * Created by sanystation on 01/11/15.
 */
public enum Zone {

    TITLE(0.3), BODY(0.7);

    private double weight;

    Zone(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

}
