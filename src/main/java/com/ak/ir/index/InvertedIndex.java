package com.ak.ir.index;

import java.io.Serializable;
import java.util.*;

/**
 * Created by olko06141 on 2.10.2015.
 */
public class InvertedIndex implements Index, Serializable {

    private static final long serialVersionUID = 3976755596080851301L;

    private Map<String, Set<Integer>> index = new TreeMap<>();

    public boolean updateIndex(String word, int docID) {
        Set<Integer> docs = index.get(word);
        if (docs == null) {
            docs = new TreeSet<>();
            docs.add(docID);
            index.put(word, docs);
            return true;
        } else {
            return docs.add(docID);
        }
    }

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        return new TreeSet<>(index.get(sentence.get(0)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index size: " + index.size());
        sb.append("[");
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            sb.append("{").append(entry.getKey()).append(" : ").append(entry.getValue()).append("}\n");
        }
        sb.append("]");
        sb.append("\n");
        return sb.toString();
    }

    public Map<String, Set<Integer>> getIndex() {
        return new TreeMap<>(index);
    }
}
