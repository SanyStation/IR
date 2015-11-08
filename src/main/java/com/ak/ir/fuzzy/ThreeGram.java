package com.ak.ir.fuzzy;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sanystation on 08/11/15.
 */
public class ThreeGram extends FuzzyIndex {

    private static final long serialVersionUID = 2632698987458271715L;

    public static final String FILE_TYPE = "kgram";

    private Map<String, Set<String>> index = new TreeMap();

    @Override
    public void update(String word) {
        if (word == null || word.isEmpty()) return;
        String modifiedWord = "$" + word + "$";
        for (int i = 0; i <= modifiedWord.length() - 3; ++i) {
            updateIndex(modifiedWord.substring(i, i + 3), word);
        }
    }

    private void updateIndex(String subWord, String word) {
        Set<String> subSet = index.get(subWord);
        if (subSet != null) subSet.add(word);
        else {
            subSet = new HashSet();
            subSet.add(word);
            index.put(subWord, subSet);
        }
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public Set<String> findWords(String pattern) {
        Set<String> resultSet = new HashSet();
        if (!pattern.startsWith("*")) pattern = "$" + pattern;
        if (!pattern.endsWith("*")) pattern = pattern + "$";
        String[] words = pattern.split("\\*");
        if (words.length > 0) {
            for (int j = 0; j <= words[0].length() - 3; ++j) {
                resultSet.addAll(index.get(words[0].substring(j, j + 3)));
            }
            for (int i = 1; i < words.length; ++i) {
                for (int j = 0; j <= words[i].length() - 3; ++j) {
                    resultSet.retainAll(index.get(words[i].substring(j, j + 3)));
                }
            }
        }
        return resultSet;
    }
}
