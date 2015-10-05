package com.ak.dictionary;

import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class IncedenceMatrix {

    private Map<String, Set<Integer>> index;
    private Map<String, Integer> documentsMap;

    public IncedenceMatrix(InvertedIndex index, DocumentsMap documentsMap) {
        this.index = index.getIndex();
        this.documentsMap = documentsMap.getDocumentsMap();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);

        StringBuilder format = new StringBuilder();
        format.append("%1$20");
        int i = 0;
        Object[] args = new Object[documentsMap.size()];
        for (Map.Entry<String, Integer> entry : documentsMap.entrySet()) {
            format.append(" %").append(++i).append("$").append("5");
        }

        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            formatter.format("%1$20s%n", entry.getKey());
//            sb.append(entry.getKey()).append(" ");
//            for (Integer docID : entry.getValue()) {
//                sb.append(docID).append(" ");
//            }
//            sb.append("\n");
        }
        return sb.toString();
    }
}
