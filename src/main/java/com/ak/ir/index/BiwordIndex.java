package com.ak.ir.index;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by sanystation on 11/10/15.
 */
public class BiwordIndex extends SavableReadable implements Index, IRObject {

    private static final long serialVersionUID = -7002992778540492459L;

    public static final String FILE_TYPE = "biwordIndex";

    private Map<String, Set<Integer>> index = new TreeMap<>();

    public boolean updateIndex(String word1, String word2, int docID) {
        String sentence = word1 + ' ' + word2;
        Set<Integer> docs = index.get(sentence);
        if (docs == null) {
            docs = new TreeSet<>();
            docs.add(docID);
            index.put(sentence, docs);
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
    protected void writeTo(PrintWriter printWriter) {
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet())
            printWriter.printf("%60s : %s%n", entry.getKey(), entry.getValue());
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {
        for (Map.Entry<String, Integer> entry : documentsMap.getDocumentsMap().entrySet()) {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(entry.getKey())));
            String line;
            while ((line = inputStream.readLine()) != null) {
                for (String symbol : IRUtils.EXTRA_SYMBOLS) line = line.replace(symbol, IRUtils.SPACE_SYMBOL);
                List<String> collection = new ArrayList(Arrays.asList(line.split(IRUtils.SPACE_SYMBOL)));
                collection.stream().forEach(IRUtils::normalize);
                for (int i = 2; i < collection.size(); ++i) {
                    updateIndex(collection.get(i - 2), collection.get(i - 1), entry.getValue());
                }
            }
            inputStream.close();
        }
    }
}
