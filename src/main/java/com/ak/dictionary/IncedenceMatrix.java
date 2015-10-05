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
        format.append("%1$20s");
        int i = 1;
        Object[] docs = new Object[documentsMap.size() + 1];
        Object[] args = new Object[documentsMap.size() + 1];
        for (Map.Entry<String, Integer> entry : documentsMap.entrySet()) {
            docs[i] = entry.getValue();
            format.append(" %").append(++i).append("$").append("5s");
        }
        docs[0] = "word \\ document";
        formatter.format(format.append('\n').toString(), docs);
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            args[0] = entry.getKey();
            for (int j = 1; j < documentsMap.size() + 1; ++j) {
                if (entry.getValue().contains(docs[j])) {
                    args[j] = 1;
                } else {
                    args[j] = 0;
                }
            }
            formatter.format(format.toString(), args);
        }
        return sb.toString();
    }
}
