package com.ak.dictionary;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by olko06141 on 2.10.2015.
 */
public class InvertedIndex implements Serializable {

    private static final long serialVersionUID = 3976755596080851301L;
    
    private Map<String, Set<Integer>> documentIndex = new TreeMap<>();

    public boolean updateIndex(String word, int docIndex) {
        Set<Integer> docs = documentIndex.get(word);
        if (docs == null) {
            docs = new TreeSet<>();
            docs.add(docIndex);
            documentIndex.put(word, docs);
            return true;
        } else {
            return docs.add(docIndex);
        }
    }

    public Set<Integer> getDocumentSet(String word) {
        return new TreeSet<>(documentIndex.get(word));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index size: " + documentIndex.size());
        sb.append("[");
        for (Map.Entry<String, Set<Integer>> entry : documentIndex.entrySet()) {
            sb.append("{").append(entry.getKey()).append(" : ").append(entry.getValue()).append("}\n");
        }
        sb.append("]");
        sb.append("\n");
        return sb.toString();
    }

    public Map<String, Set<Integer>> getIndex() {
        return new TreeMap<>(documentIndex);
    }
}
