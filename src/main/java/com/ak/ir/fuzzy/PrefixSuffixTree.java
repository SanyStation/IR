package com.ak.ir.fuzzy;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sanystation on 07/11/15.
 */
public class PrefixSuffixTree extends SavableReadable implements IRObject {

    private static final long serialVersionUID = 395181853583756999L;

    public static final String FILE_TYPE = "prefixSuffixTree";

    private Map<String, Set<String>> prefixTree = new TreeMap();
    private Map<String, Set<String>> suffixTree = new TreeMap();

    public void update(String word) {
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

    public static void main(String[] args) {
        PrefixSuffixTree pfTree = new PrefixSuffixTree();
        pfTree.update("ragul");
        pfTree.update("ragal");
        System.out.println(pfTree.findWords("rag*l"));
    }

    public Set<String> findWords(String pattern) {
        Set<String> resultSet = new HashSet();
        if (!pattern.contains("\\*") && pattern.contains("*")) {
            String[] words = pattern.split("\\*");
            if (words.length == 2) {
                Set<String> pSet = prefixTree.get(words[0]);
                Set<String> sSet = suffixTree.get(words[1]);
                resultSet.addAll(pSet != null ? pSet : new HashSet());
                resultSet.retainAll(sSet != null ? sSet : new HashSet());
            } else if (words.length == 1) resultSet.addAll(prefixTree.get(words[0]));
        } else if (!pattern.isEmpty()) resultSet.addAll(prefixTree.get(pattern));
        return resultSet;
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {

    }

    @Override
    protected void writeTo(PrintWriter printWriter) {

    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }
}
