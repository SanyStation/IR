package com.ak.ir.index;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by olko06141 on 2.10.2015.
 */
public class InvertedIndex extends SavableReadable implements Index, IRObject {

    private static final long serialVersionUID = 3976755596080851301L;

    public static final String FILE_TYPE = "invertedIndex";
    private Map<String, Set<Integer>> index = new TreeMap<>();

    public void bulkUpdate(Collection<String> arrayOfWords, int docID) {
        arrayOfWords.forEach(word -> this.update(word, docID));
    }

    public boolean update(String word, int docID) {
        Set<Integer> docs = index.get(word);
        if (docs == null) {
            docs = new TreeSet<>();
            docs.add(docID);
            index.put(word, docs);
            return true;
        } else {
            return docs.add(docID);
        }
    }

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        return new TreeSet<>(index.get(sentence.get(0)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index size: " + index.size());
        sb.append("[");
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            sb.append("{").append(entry.getKey()).append(" : ").append(entry.getValue()).append("}\n");
        }
        sb.append("]");
        sb.append("\n");
        return sb.toString();
    }

    public Map<String, Set<Integer>> getIndex() {
        return new TreeMap<>(index);
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet())
            printWriter.printf("%30s : %s%n", entry.getKey(), entry.getValue());
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }

    @Override
    protected InvertedIndex readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (InvertedIndex) ois.readObject();
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {
        for (Map.Entry<String, Integer> entry : documentsMap.getDocumentsMap().entrySet()) {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(entry.getKey())));
            String line;
            while ((line = inputStream.readLine()) != null) {
                for (String symbol : IRUtils.EXTRA_SYMBOLS) line = line.replace(symbol, IRUtils.SPACE_SYMBOL);
                Collection<String> collection = new ArrayList(Arrays.asList(line.split(IRUtils.SPACE_SYMBOL)));
                collection.stream().forEach(IRUtils::normalize);
                bulkUpdate(collection, entry.getValue());
            }
            inputStream.close();
        }
    }
}
