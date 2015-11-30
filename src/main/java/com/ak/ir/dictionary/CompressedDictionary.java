package com.ak.ir.dictionary;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by olko06141 on 30.11.2015.
 */
public class CompressedDictionary extends SavableReadable implements IRObject {

    private static final long serialVersionUID = 1263484679844090143L;

    public static final int DICTIONARY_INIT_SIZE = 10;
    public static final String FILE_TYPE = "dictionary";
    private int position = 0;
    private char[] dictionary = new char[DICTIONARY_INIT_SIZE];
    private Map<Integer, Integer> table = new HashMap<>();

    private void increaseDictionarySize() {
        char[] newDictionary = new char[dictionary.length * 3 / 2 + 1];
        System.arraycopy(dictionary, 0, newDictionary, 0, dictionary.length);
        dictionary = newDictionary;
    }

    public void bulkUpdate(Collection<String> arrayOfWords) {
        arrayOfWords.forEach(this::update);
    }

    public boolean update(String word) {
        if (word != null && word.isEmpty()) return false;
        int pos = findWord(word);
        if (pos == -1) {
            while (position + word.length() >= dictionary.length) increaseDictionarySize();
            for (int i = 0; i < word.length(); ++i, ++position) dictionary[position] = word.charAt(i);
            table.put(position - word.length(), 1);
            return true;
        } else table.put(pos, table.get(pos) + 1);
        return false;
    }

    private int findWord(String word) {
        int j = 0;
        int pos = -1;
        for (int i = 0; i < position; ++i) {
            if (j < word.length()) {
                if (dictionary[i] == word.charAt(j)) {
                    pos = i - j;
                    ++j;
                } else {
                    i -= j;
                    j = 0;
                    pos = -1;
                }
            } else return pos;
        }
        return pos;
    }

    @Override
    public String toString() {
        return "Dictionary size: " + getSize() + " word(s)";
    }

    public String[] getWords() {
        String[] newArray = new String[position];
        System.arraycopy(dictionary, 0, newArray, 0, position);
        return newArray;
    }

    public int getSize() {
        return position;
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
//        for (String word : getWords()) printWriter.printf("%1s%n", word);
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
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
