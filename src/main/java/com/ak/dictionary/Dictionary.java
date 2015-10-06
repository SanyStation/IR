package com.ak.dictionary;

import java.io.Serializable;
import java.util.*;

/**
 * Created by olko06141 on 20.9.2015.
 */
public class Dictionary implements Serializable {

    public static final int DICTIONARY_INIT_SIZE = 10;

    private InvertedIndex invertedIndex = new InvertedIndex();
    private DocumentsMap documentsMap = new DocumentsMap();
    private String[] dictionary = new String[DICTIONARY_INIT_SIZE];
    private int index = 0;

    private void increaseDictionarySize() {
        String[] newDictionary = new String[dictionary.length * 3 / 2 + 1];
        System.arraycopy(dictionary, 0, newDictionary, 0, dictionary.length);
        dictionary = newDictionary;
    }

    public void addArrayOfWords(String[] arrayOfWords, String documentName) {
        int docIndex =  documentsMap.addDocument(documentName);
        for (String word : arrayOfWords) addWord(word, docIndex);
    }

    public boolean addWord(String word, int docIndex) {
        if (word == null) return false;
        word = normalize(word);
        if (word.isEmpty()) return false;
        invertedIndex.updateIndex(word, docIndex);
        if (!findWord(word)) {
            if (index >= dictionary.length) {
                increaseDictionarySize();
            }
            dictionary[index] = word;
            ++index;
            return true;
        }
        return false;
    }

    private boolean findWord(String word) {
        for (int i = 0; i < index; ++i) {
            if (dictionary[i].equals(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        sortDictionary();
        StringBuilder sb = new StringBuilder();
        sb.append("Dictionary size: " + index + " word(s)");
        return sb.toString();
    }

    public String[] getWords() {
        sortDictionary();
        String[] newArray = new String[index];
        System.arraycopy(dictionary, 0, newArray, 0, index);
        return newArray;
    }

    private String normalize(String word) {
        if (word.endsWith(".") || word.endsWith(":")) {
            word = word.substring(0, word.length() - 1);
        }
        return word.toLowerCase();
    }

    public void sortDictionary() {
        Arrays.sort(dictionary, 0, index, (o1, o2) -> o1.compareTo(o2));
    }

    public int getSize() {
        return index;
    }

    public Map<String, Set<Integer>> getIndex() {
        return invertedIndex.getIndex();
    }

    public Map<String, Integer> getDocumentsMap() {
        return documentsMap.getDocumentsMap();
    }

}
