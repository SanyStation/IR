package com.ak.dictionary;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class IncedenceMatrix {

    public static final int ZERO = 0;
    public static final int ONE = 1;

    private static class Entry<W, A> {

        W word;
        A docIDs;

        private Entry(W word, A docIDs) {
            this.word = word;
            this.docIDs = docIDs;
        }
    }

    private Map<String, Set<Integer>> index;
    private Map<String, Integer> documentsMap;
    private TreeSet<Entry<String, Integer[]>> matrix = new TreeSet<>();

    public IncedenceMatrix(InvertedIndex index, DocumentsMap documentsMap) {
        this.index = index.getIndex();
        this.documentsMap = documentsMap.getDocumentsMap();
    }

    public boolean addWord(String word) {
        if (!matrix.contains(word) && index.containsValue(word)) {
            Set<Integer> docIDs = index.get(word);
            Integer[] docArray = new Integer[documentsMap.size()];
            for (int i = 0; i < docArray.length; ++i) {
                if (docIDs.contains(i)) docArray[i] = 1;
                else docArray[i] = 0;
            }
            matrix.add(new Entry<>(word, docArray));
            return true;
        } else
            return false;
    }

    public int[] calculateBineryDocuments(String sentence) {
        String[] words = sentence.split(FileProcessor.SPACE_SYMBOL);
        for (String word : words) {

        }
        return null;
    }

    private Integer[] getBinaryArray(String word) {
        return null;
    }

    private Integer[] invertBinaryArray(@NotNull Integer[] array) {
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) result[i] = array[i].equals(ONE) ? ZERO : ONE;
        return result;
    }

}
