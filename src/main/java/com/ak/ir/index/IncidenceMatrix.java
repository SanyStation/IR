package com.ak.ir.index;

import com.ak.ir.DocumentsMap;
import com.ak.ir.IRObject;
import com.ak.ir.SavableReadable;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.*;

/**
 * Created by olko06141 on 1.10.2015.
 */
public class IncidenceMatrix extends SavableReadable implements Index, IRObject {

    private static final long serialVersionUID = -2728255565587517055L;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final String NOT = "!";
    public static final String FILE_TYPE = "incidenceMatrix";
    private Map<String, Integer> docMap;
    private TreeMap<String, Integer[]> matrix = new TreeMap<>();

    public void bulkUpdate(Collection<String> arrayOfWords, int docID) {
        arrayOfWords.forEach(word -> this.update(word, docID));
    }

    public boolean update(String word, int docID) {
        int pos = docID - 1;
        Integer[] docArray = matrix.get(word);
        if (docArray == null) {
            docArray = new Integer[docMap.size()];
            docArray[pos] = ONE;
            matrix.put(word, docArray);
            return true;
        } else {
            System.out.println(Arrays.toString(docArray));
            if (docArray[pos] == ZERO) {
                docArray[pos] = ONE;
                return true;
            } else return false;
        }
    }

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        Set<Integer> documents = new TreeSet<>();
//        Integer[] docArray = calculateBinaryDocuments(sentence);
//        for (int i = 0; i < docArray.length; ++i) {
//            if (docArray[i].equals(ONE)) {
//                for (Map.Entry<String, Integer> entry : docMap.entrySet()) {
//                    if (entry.getValue().equals(i + 1)) documents.add(entry.getKey());
//                }
//            }
//        }
        return documents;
    }

    private Integer[] calculateBinaryDocuments(String sentence) {
        String[] words = sentence.split(IRUtils.SPACE_SYMBOL);
        Integer[] array = bitwiseOperationINVERSE(getBitArray(NOT));
        for (String word : words)
            if (!word.contains(NOT)) array = bitwiseOperationAND(array, getBitArray(word));
            else array = bitwiseOperationAND(array, bitwiseOperationINVERSE(getBitArray(word.replace(NOT, ""))));
        return array;
    }

    private Integer[] bitwiseOperationAND(Integer[] array1, Integer[] array2) {
        if (array1.length != array2.length)
            throw new IllegalArgumentException("[IncidenceMatrix] The arrays have to have the same length. " +
                    "But array1 length is " + array1.length + " and array2 length is " + array2.length);
        Integer[] result = new Integer[array1.length];
        for (int i = 0; i < array1.length; ++i) result[i] = array1[i] * array2[i];
        return result;
    }

    private Integer[] getBitArray(String word) {
        Integer[] result = new Integer[docMap.size()];
        if (matrix.containsKey(word)) result = matrix.get(word);
        else for (int i = 0; i < result.length; ++i) result[i] = ZERO;
        return result;
    }

    private Integer[] bitwiseOperationINVERSE(Integer[] array) {
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; ++i) result[i] = array[i].equals(ONE) ? ZERO : ONE;
        return result;
    }

    @Override
    public void buildIRObject(DocumentsMap documentsMap) throws IOException {
        docMap = documentsMap.getDocumentsMap();
        for (Map.Entry<String, Integer> entry : docMap.entrySet()) {
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
    protected void writeTo(PrintWriter printWriter) {
        StringBuilder format = new StringBuilder();
        format.append("%1$20s |");
        int i = 1;
        Object[] docs = new Object[docMap.size() + 1];
        Object[] args = new Object[docMap.size() + 1];
        for (Map.Entry<String, Integer> entry : docMap.entrySet()) {
            docs[i] = entry.getValue();
            format.append(" %").append(++i).append("$5s |");
        }
        docs[0] = "word \\ document";
        printWriter.format(format.append('\n').toString(), docs);
        for (Map.Entry<String, Integer[]> entry : matrix.entrySet()) {
            args[0] = entry.getKey();
            Integer[] docArray = entry.getValue();
            for (int j = 0; j < docArray.length; ++j) args[j + 1] = docArray[j] == ONE ? ONE : ZERO;
            printWriter.format(format.toString(), args);
        }
        printWriter.close();
    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }

    @Override
    protected IncidenceMatrix readFrom(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (IncidenceMatrix) ois.readObject();
    }
}
