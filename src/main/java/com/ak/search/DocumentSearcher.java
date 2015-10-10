package com.ak.search;

import com.ak.dictionary.Dictionary;
import com.ak.dictionary.IncidenceMatrix;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
