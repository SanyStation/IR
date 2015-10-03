package com.ak.dictionary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class DocumentsMap implements Serializable {

    private Map<String, Integer> map = new HashMap<>();

    public int addDocument(String fullName) {
        int index;
        if (map.isEmpty()) {
            index = 1;
            map.put(fullName, index);
        } else {
            Integer value = map.get(fullName);
            index = map.size() + 1;
            if (value == null) map.put(fullName, index);
        }
        System.out.println("File name = " + fullName + "; index = " + index);
        return index;
    }

    public Map<String, Integer> getDocumentsMap() {
        return new HashMap<>(map);
    }

    @Override
    public String toString() {
        return "DocumentList{list = " + map + "}";
    }

}
