package com.ak.dictionary;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class IncedenceMatrix {

    private int[][] matrix;

    public IncedenceMatrix(int documents, int words) {
        if (documents > 0 && words > 0) matrix = new int[documents][words];
    }

}
