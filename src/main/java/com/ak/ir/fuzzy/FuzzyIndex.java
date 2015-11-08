package com.ak.ir.fuzzy;

import com.ak.ir.SavableReadable;

import java.util.Collection;
import java.util.Set;

/**
 * Created by sanystation on 08/11/15.
 */
public abstract class FuzzyIndex extends SavableReadable {

    public abstract Set<String> findWords(String pattern);

    public abstract void update(String word);

    public void bulkUpdate(Collection<String> words) {
        words.stream().forEach(this::update);
    }
}
