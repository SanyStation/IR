package com.ak.search;

import com.ak.dictionary.Dictionary;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by olko06141 on 6.10.2015.
 */
public class DocumentSearcher {

    private Dictionary dictionary;

    public DocumentSearcher(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Set<Integer> findDocumentIndexes(String word) {
        return dictionary.getIndex().getOrDefault(word, new TreeSet<>());
    }

    public Set<String> findDocuments(String word) {
        Set<Integer> docIDs = findDocumentIndexes(word);
        Set<String> docs = new HashSet<>();
        Map<String, Integer> documentsMap =  dictionary.getDocumentsMap();
        docs.addAll(documentsMap.entrySet().stream().filter(entry -> docIDs.contains(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList()));
        return docs;
    }

}
