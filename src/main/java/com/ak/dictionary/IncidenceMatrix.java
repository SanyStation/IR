package com.ak.dictionary;

import com.sun.istack.internal.NotNull;

import java.util.*;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class IncidenceMatrix {

    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final String NOT = "!";

    private Map<String, Set<Integer>> index;
    private Map<String, Integer> documentsMap;
    private TreeMap<String, Integer[]> matrix = new TreeMap<>();

    public IncidenceMatrix(InvertedIndex index, DocumentsMap documentsMap) {
        this.index = index.getIndex();
        this.documentsMap = documentsMap.getDocumentsMap();
        for (Map.Entry<String, Set<Integer>> entry : this.index.entrySet()) addWord(entry.getKey());
    }

    public boolean addWord(String word) {
        if (!matrix.containsKey(word) && index.containsValue(word)) {
            Set<Integer> docIDs = index.get(word);
            Integer[] docArray = new Integer[documentsMap.size()];
            for (int i = 0; i < docArray.length; ++i) {
                if (docIDs.contains(i)) docArray[i] = ONE;
                else docArray[i] = ZERO;
            }
            matrix.put(word, docArray);
            return true;
        } else
            return false;
    }

    public Set<String> getDocuments(String sentence) {
        Set<String> documents = new TreeSet<>();
        Integer[] docArray = calculateBinaryDocuments(sentence);
        for (int i = 0; i < docArray.length; ++i) {
            if (docArray[i].equals(ONE)) {
                for (Map.Entry<String, Integer> entry : documentsMap.entrySet()) {
                    if (entry.getValue().equals(i)) documents.add(entry.getKey());
                }
            }
        }
        return documents;
    }

    private Integer[] calculateBinaryDocuments(String sentence) {
        String[] words = sentence.split(FileProcessor.SPACE_SYMBOL);
        Integer[] array = invertBitArray(getBitArray(NOT));
        for (String word : words) {
            if (!word.contains(NOT)) array = bitwiseOperationAND(array, getBitArray(word));
            else array = bitwiseOperationAND(array, invertBitArray(getBitArray(word)));
        }
        return array;
    }

    private Integer[] bitwiseOperationAND(Integer[] array1, Integer[] array2) {
        if (array1.length != array2.length)
            throw new IllegalArgumentException("[Dictionary] The arrays have to have the same length. " +
                    "But array1 length is " + array1.length + " and array2 length is " + array2.length);
        Integer[] result = new Integer[array1.length];
        for (int i = 0; i < array1.length; ++i) result[i] = array1[i] * array2[i];
        return result;
    }

    private Integer[] getBitArray(String word) {
        Integer[] result = new Integer[documentsMap.size()];
        if (matrix.containsKey(word)) result = matrix.get(word);
        else for (int i = 0; i < result.length; ++i) result[i] = ZERO;
        return result;
    }

    private Integer[] invertBitArray(@NotNull Integer[] array) {
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) result[i] = array[i].equals(ONE) ? ZERO : ONE;
        return result;
    }

}
