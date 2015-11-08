package com.ak.ir.fuzzy;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by sanystation on 07/11/15.
 */
public class PrefixSuffixTree extends FuzzyIndex {

    private static final long serialVersionUID = 395181853583756999L;

    public static final String FILE_TYPE = "prefixSuffixTree";

    private Map<String, Set<String>> prefixTree = new TreeMap();
    private Map<String, Set<String>> suffixTree = new TreeMap();

    @Override
    public void update(String word) {
        if (word == null) return;
        for (int i = 1; i <= word.length(); ++i) updateTree(word, word.substring(0, i), prefixTree);
        for (int i = word.length() - 1; i >= 0; --i) updateTree(word, word.substring(i, word.length()), suffixTree);
    }

    private void updateTree(String word, String subWord, Map<String, Set<String>> tree) {
        Set<String> subSet = tree.get(subWord);
        if (subSet != null) subSet.add(word);
        else {
            subSet = new HashSet();
            subSet.add(word);
            tree.put(subWord, subSet);
        }
    }

    @Override
    public Set<String> findWords(String pattern) {
        Set<String> resultSet = new HashSet();
        if (pattern.contains("*")) {
            String[] words = pattern.split("\\*");
            if (words.length >= 2) {
                Set<String> pSet = prefixTree.get(words[0]);
                Set<String> sSet = suffixTree.get(words[words.length - 1]);
                resultSet.addAll(pSet != null ? pSet : new HashSet());
                resultSet.retainAll(sSet != null ? sSet : new HashSet());
                for (int i = 1; i < words.length - 1; ++i) {
                    String subWord = words[i];
                    resultSet = resultSet.stream().filter(word -> word.contains(subWord)).collect(Collectors.toSet());
                }
            } else if (words.length == 1) resultSet.addAll(prefixTree.get(words[0]));
        } else if (!pattern.isEmpty()) resultSet.addAll(prefixTree.get(pattern));
        return resultSet;
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }
}
