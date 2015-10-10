package com.ak.dictionary;

import java.io.Writer;

/**
 * Created by sanystation on 10/10/15.
 */
public abstract class Saveable {

    public abstract void saveToFile(Writer writer, String directory);

}
