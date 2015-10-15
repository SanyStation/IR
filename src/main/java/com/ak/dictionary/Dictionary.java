package com.ak.dictionary;

import com.ak.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by olko06141 on 20.9.2015.
 */
public class Dictionary extends SavableReadable {

    private static final long serialVersionUID = -6930522145462340882L;

    public static final int DICTIONARY_INIT_SIZE = 10;

    protected String fileType = "dictionary";
    private String[] dictionary = new String[DICTIONARY_INIT_SIZE];
    private int position = 0;

    private void increaseDictionarySize() {
        String[] newDictionary = new String[dictionary.length * 3 / 2 + 1];
        System.arraycopy(dictionary, 0, newDictionary, 0, dictionary.length);
        dictionary = newDictionary;
    }

    public void addArrayOfWords(String[] arrayOfWords) {
        for (String word : arrayOfWords) addWord(word);
    }

    public boolean addWord(String word) {
        word = IRUtils.normalize(word);
        if (word.isEmpty()) return false;
        if (!isWordFound(word)) {
            if (position >= dictionary.length) {
                increaseDictionarySize();
            }
            dictionary[position] = word;
            ++position;
            return true;
        }
        return false;
    }

    private boolean isWordFound(String word) {
        for (int i = 0; i < position; ++i) {
            if (dictionary[i].equals(word)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dictionary size: " + getSize() + " word(s)";
    }

    public String[] getWords() {
        sortDictionary();
        String[] newArray = new String[position];
        System.arraycopy(dictionary, 0, newArray, 0, position);
        return newArray;
    }

    public void sortDictionary() {
        Arrays.sort(dictionary, 0, position, (o1, o2) -> o1.compareTo(o2));
    }

    public int getSize() {
        return position;
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
        for (String word : getWords()) printWriter.printf("%1s%n", word);
    }

    @Override
    protected String getFileType() {
        return fileType;
    }

    @Override
    protected Dictionary readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Dictionary) ois.readObject();
    }
}
