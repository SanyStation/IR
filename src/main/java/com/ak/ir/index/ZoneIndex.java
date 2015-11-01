package com.ak.ir.index;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IOUtils;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by sanystation on 01/11/15.
 */
public class ZoneIndex extends SavableReadable implements Index, IRObject {

    private static final long serialVersionUID = 331800687751004046L;

    public static final String FILE_TYPE = "zoneIndex";

    private Map<String, Map<Integer, Set<Zone>>> index = new TreeMap<>();

    public void bulkUpdate(Collection<String> arrayOfWords, int docID) {
        arrayOfWords.forEach(word -> this.update(word, docID, Zone.BODY));
    }

    public boolean update(String word, int docID, Zone zone) {
        Map<Integer, Set<Zone>> docs = index.get(word);
        if (docs == null) {
            Set<Zone> zones = new TreeSet();
            zones.add(zone);
            docs = new TreeMap<>();
            docs.put(docID,zones);
            index.put(word, docs);
            return true;
        } else {
            Set<Zone> zones = docs.get(docID);
            if (zones == null) {
                zones = new TreeSet();
                zones.add(zone);
                docs.put(docID, zones);
                return true;
            } else {
                if (zones.contains(zone)) return false;
                else {
                    zones.add(zone);
                    return true;
                }
            }
        }
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {
        for (Map.Entry<String, Integer> entry : documentsMap.getDocumentsMap().entrySet()) {
            File file = new File(entry.getKey());
            String fileName = IOUtils.cutFileExtension(file.getName());
            System.out.println(fileName);
            update(fileName, entry.getValue(), Zone.TITLE);
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

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        return null;
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {
        for (Map.Entry<String, Map<Integer, Set<Zone>>> entry : index.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, Set<Zone>> innerEntry : entry.getValue().entrySet())
                sb.append(" ").append(innerEntry.getKey()).append(" ").append(innerEntry.getValue()).append(",");
            printWriter.printf("%30s : %s%n", entry.getKey(), sb.subSequence(0, sb.length() - 1));
        }
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }
}
