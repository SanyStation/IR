package com.ak.ir.fuzzy;

import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sanystation on 08/11/15.
 */
public class PermutermIndex extends FuzzyIndex {

    private static final long serialVersionUID = -4310266244050414052L;

    public static final String FILE_TYPE = "permutermIndex";

    private Map<String, String> index = new HashMap();

    @Override
    public Set<String> findWords(String pattern) {
        Set<String> resultSet = new HashSet();
        if (!pattern.endsWith("*")) pattern = pattern + "$";
        if (pattern.contains("*")) {
            int pos = pattern.indexOf("*");
            pattern = (pattern.substring(pos, pattern.length()) + pattern.substring(0, pos)).replace("*", "");
        }
        for (Map.Entry<String, String> entry : index.entrySet()) if (entry.getKey().startsWith(pattern)) resultSet.add(entry.getValue());
        return resultSet;
    }

    @Override
    public void update(String word) {
        if (word == null || word.isEmpty()) return;
        String modifiedWord = word + "$";
        for (int i = 0; i <= modifiedWord.length(); ++i) {
            StringBuilder sb = new StringBuilder();
            sb.append(modifiedWord.substring(i, modifiedWord.length())).append(modifiedWord.substring(0, i));
            updateIndex(sb.toString(), word);
        }
    }

    private void updateIndex(String subWord, String word) {
        String resWord = index.get(subWord);
        if (resWord == null) index.put(subWord, word);
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {}

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }
}
