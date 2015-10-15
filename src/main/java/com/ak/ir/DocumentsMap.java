package com.ak.ir;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class DocumentsMap extends SavableReadable {

    private static final long serialVersionUID = 4875130577590892949L;
    protected String fileType = "docmap";
    private Map<String, Integer> map = new TreeMap<>();

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
        return index;
    }

    public Map<String, Integer> getDocumentsMap() {
        return new HashMap<>(map);
    }

    @Override
    public String toString() {
        return "DocumentsMap size: " + map.size() + " word(s)";
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
        for (Map.Entry<String, Integer> entry : map.entrySet())
            printWriter.printf("%5d : %s%n", entry.getValue(), entry.getKey());
    }

    @Override
    protected String getFileType() {
        return fileType;
    }

    @Override
    protected DocumentsMap readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (DocumentsMap) ois.readObject();
    }
}
