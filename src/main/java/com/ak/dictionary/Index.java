package com.ak.dictionary;

import java.util.List;
import java.util.Set;

/**
 * Created by sanystation on 10/10/15.
 */
public interface Index {

    public Set<Integer> findDocumentSet(List<String> sentence);

}
