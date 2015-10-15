package com.ak.ir.search;

import com.ak.ir.index.IncidenceMatrix;

import java.util.Set;

/**
 * Created by olko06141 on 6.10.2015.
 */
public class DocumentSearcher {

    private IncidenceMatrix matrix;

    public DocumentSearcher(IncidenceMatrix matrix) {
        this.matrix = matrix;
    }

    public Set<String> findDocuments(String sentence) {
        return matrix.findDocuments(sentence);
    }

}
