package com.ak.ir.dictionary;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by olko06141 on 20.9.2015.
 */
public class Dictionary extends SavableReadable implements IRObject {

    private static final long serialVersionUID = -6930522145462340882L;
    
    public static final int DICTIONARY_INIT_SIZE = 10;
    public static final String FILE_TYPE = "dictionary";
    private String[] dictionary = new String[DICTIONARY_INIT_SIZE];
    private int position = 0;

    private void increaseDictionarySize() {
        String[] newDictionary = new String[dictionary.length * 3 / 2 + 1];
        System.arraycopy(dictionary, 0, newDictionary, 0, dictionary.length);
        dictionary = newDictionary;
    }

    public void bulkUpdate(Collection<String> arrayOfWords) {
        arrayOfWords.forEach(this::update);
    }

    public boolean update(String word) {
        if (word != null && word.isEmpty()) return false;
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
        return FILE_TYPE;
    }

    @Override
    protected Dictionary readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (Dictionary) ois.readObject();
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {
        for (Map.Entry<String, Integer> entry : documentsMap.getDocumentsMap().entrySet()) {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(entry.getKey())));
            String line;
            while ((line = inputStream.readLine()) != null) {
                for (String symbol : IRUtils.EXTRA_SYMBOLS) line = line.replace(symbol, IRUtils.SPACE_SYMBOL);
                Collection<String> collection = new ArrayList(Arrays.asList(line.split(IRUtils.SPACE_SYMBOL)));
                collection.stream().forEach(IRUtils::normalize);
                bulkUpdate(collection);
            }
            inputStream.close();
        }
    }
}
