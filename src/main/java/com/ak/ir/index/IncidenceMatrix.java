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
    private TreeMap<String, Integer[]> matrix = new TreeMap<>();

    public void bulkUpdate(Collection<String> arrayOfWords, int docID) {
        arrayOfWords.forEach(word -> this.update(word, docID));
    }

    public boolean update(String word, int docID) {
        if (!matrix.containsKey(word)) {
            Integer[] docArray = new Integer[documentsMap.size()];
            for (int i = 0; i < docArray.length; ++i) {
                if (docIDs.contains(i + 1)) docArray[i] = ONE;
                else docArray[i] = ZERO;
            }
            matrix.put(word, docArray);
            return true;
        } else
            return false;
    }

    public Set<String> findDocuments(String sentence) {
        Set<String> documents = new TreeSet<>();
        Integer[] docArray = calculateBinaryDocuments(sentence);
        for (int i = 0; i < docArray.length; ++i) {
            if (docArray[i].equals(ONE)) {
                for (Map.Entry<String, Integer> entry : documentsMap.entrySet()) {
                    if (entry.getValue().equals(i + 1)) documents.add(entry.getKey());
                }
            }
        }
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
            throw new IllegalArgumentException("[Dictionary] The arrays have to have the same length. " +
                    "But array1 length is " + array1.length + " and array2 length is " + array2.length);
        Integer[] result = new Integer[array1.length];
        for (int i = 0; i < array1.length; ++i) result[i] = array1[i] * array2[i];
        return result;
    }

    private Integer[] getBitArray(String word) {
        Integer[] result = new Integer[documentsMap.size()];
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

    @Override
    public Set<Integer> findDocumentSet(List<String> sentence) {
        return null;
    }

    @Override
    protected void writeTo(PrintWriter printWriter) {

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
