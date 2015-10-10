package com.ak.dictionary;

import java.io.Serializable;
import java.util.*;

/**
 * Created by sanystation on 11/10/15.
 */
public class BiwordIndex implements Index, Serializable {

    //TODO put UID

    private Map<String, Set<Integer>> index = new TreeMap<>();

    public boolean updateIndex(String word1, String word2, int docID) {
        String sentence = word1 + ' ' + word2;
        Set<Integer> docs = index.get(sentence);
        if (docs == null) {
            docs = new TreeSet<>();
            docs.add(docID);
            index.put(sentence, docs);
            return true;
        } else {
            return docs.add(docID);
        }
    }

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        return new TreeSet<>(index.get(sentence.get(0)));
    }
}
