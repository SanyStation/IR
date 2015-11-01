package com.ak.ir.index;

import com.ak.ir.SavableReadable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sanystation on 11/10/15.
 */
public class BiwordIndex extends SavableReadable implements Index {

    private static final long serialVersionUID = -7002992778540492459L;

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

    @Override
    protected void writeTo(PrintWriter printWriter) {

    }

    @Override
    protected String getFileType() {
        return null;
    }
}
