package com.ak.ir.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by olko06141 on 15.10.2015.
 */
public class IRUtils {

    public static final String[] EXTRA_SYMBOLS = new String[]{
            System.lineSeparator(), ",", "(", ")", "{", "}", "\"", "'", "“", ";",
            "[", "]", "\t", "—", "!", ".", "#", "&", "?", "|", "//", "--", "*", "\n", "\r", "..."};
    public static final String SPACE_SYMBOL = " ";

    public static String normalize(String word) {
        if (word == null) return "";
        if (word.endsWith(".") || word.endsWith(":")) {
            word = word.substring(0, word.length() - 1);
        }
        return word.toLowerCase();
    }

    public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = (k1, k2) -> {
            int compare = map.get(k2).compareTo(map.get(k1));
            if (compare == 0) return 1;
            else return compare;
        };
        Map<K, V> sortedByValues = new TreeMap(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }

}
