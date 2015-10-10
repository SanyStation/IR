package com.ak.dictionary;

import java.io.Serializable;
import java.util.*;

/**
 * Created by olko06141 on 9.10.2015.
 */
public class CoordinateInvertedIndex implements Index, Serializable {

    //TODO put UID

    private Map<String, Map<Integer, Set<Integer>>> index = new TreeMap<>();

    public boolean addWord(String word, Integer docID, Integer pos) {
        Map<Integer, Set<Integer>> docs = index.get(word);
        if (docs == null) {
            docs = new TreeMap<>();
            docs.put(docID, new TreeSet<>(Arrays.asList(pos)));
            index.put(word, docs);
            return true;
        } else {
            Set<Integer> positions = docs.get(docID);
            if (positions == null) positions = new TreeSet<>();
            return positions.add(pos);
        }
    }

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        Set<Integer> result = new TreeSet<>();
        Map<Integer, Set<Integer>> docs = index.get(sentence.get(0));
        Map<Integer, Set<Integer>> resultMap = new TreeMap<>(docs);

        if (docs == null) return result;

        for (int i = 1; i < sentence.size(); ++i) {
            docs = index.get(sentence.get(i));
            if (docs == null) {
                result.clear();
                break;
            }
            resultMap = mapIntersection(resultMap, docs);
        }
        result = resultMap.keySet();
        return result;
    }

    private Map<Integer, Set<Integer>> mapIntersection(Map<Integer, Set<Integer>> map1, Map<Integer, Set<Integer>> map2) {
        Map<Integer, Set<Integer>> result = new TreeMap<>();
        Set<Integer> set1 = map1.keySet();
        Set<Integer> set2 = map2.keySet();
        set1.retainAll(set2);
        for (Integer docID : set1) {
            Set<Integer> positions1 = map1.get(docID);
            Set<Integer> positions2 = map2.get(docID);
            Iterator<Integer> iterator1 = positions1.iterator();
            Iterator<Integer> iterator2 = positions2.iterator();
            outer: while (iterator1.hasNext()) {
                Integer value1 = iterator1.next();
                while (iterator2.hasNext()) {
                    Integer value2 = iterator2.next();
                    if (value1.equals(value2 - 1)) {
                        result.put(docID, positions2);
                        break outer;
                    }
                }
            }
        }
        return result;
    }
}
