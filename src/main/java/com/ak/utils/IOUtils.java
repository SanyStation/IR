package com.ak.utils;

import com.ak.dictionary.Dictionary;
import com.ak.dictionary.IncidenceMatrix;
import com.ak.dictionary.InvertedIndex;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by olko06141 on 6.10.2015.
 */
public class IOUtils {

    public static final String DATE_MASK = "dd.MM.yyyy_HH.mm.ss";
    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_OUT = "out";
    public static final String TYPE_DICTIONARY = "dictionary";
    public static final String TYPE_MATRIX = "matrix";
    public static final int KILOBYTE = 1024;

    public static void saveDictionary(Dictionary dictionary, String directory) throws IOException {
        directory = normalizeAddress(directory);
        serializeDictionary(dictionary, directory);
        saveToTextFileDictionary(dictionary, directory);
        saveToTextFileIncidenceMatrix(dictionary.getIncidenceMatrix(), directory);
    }

    private static void serializeDictionary(Dictionary dictionary, String directory) throws IOException {
        String fileName = generateFileName(directory, TYPE_DICTIONARY, EXTENSION_OUT);
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dictionary);
        oos.flush();
        oos.close();
    }

    public static void saveToTextFileDictionary(Dictionary dictionary, String directory) throws IOException {
        String fileName = generateFileName(directory, TYPE_DICTIONARY, EXTENSION_TXT);
        Writer fileWriter = new BufferedWriter(new java.io.FileWriter(fileName, false));
        PrintWriter out = new PrintWriter(fileWriter);
        out.println("Index:");
        for (Map.Entry<String, Integer> entry : dictionary.getDocumentsMap().entrySet())
            out.printf("Document name: %s; document index: %s%n", entry.getKey(), entry.getValue());
        out.printf("Dictionary size: %s word(s) %n %n", dictionary.getSize());
        Map<String, Set<Integer>> index = dictionary.getIndex();
        for (String word : dictionary.getWords()) out.printf("%s : %s %n", word, index.get(word));
        out.close();

        File file = new File(fileName);
        System.out.println("Total file size " + Math.round((double) file.length() / KILOBYTE) + " KB");
    }

    private static String generateFileName(String directory, String type, String extension) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MASK);
        String formattedDate = sdf.format(date);
        StringBuilder sb = new StringBuilder(directory);
        sb.append("/").append(type).append("_").append(formattedDate).append(".").append(extension);
        return sb.toString();
    }

    private static String normalizeAddress(String address) {
        if (address.contains("/")) return address.replace("/", File.separator);
        if (address.contains("\\")) return address.replace("\\", File.separator);
        return address;
    }

    public static void saveToTextFileIncidenceMatrix(IncidenceMatrix matrix, String directory) throws IOException {
        String fileName = generateFileName(directory, TYPE_MATRIX, EXTENSION_TXT);
        Writer fileWriter = new BufferedWriter(new java.io.FileWriter(fileName, false));
        Map<String, Set<Integer>> index = matrix.getIndex();
        Map<String, Integer> documentsMap = matrix.getDocumentsMap();
        PrintWriter out = new PrintWriter(fileWriter);
        out.println("Index:");
        for (Map.Entry<String, Integer> entry : documentsMap.entrySet())
            out.printf("Document name: %s; document index: %s%n", entry.getKey(), entry.getValue());
        StringBuilder format = new StringBuilder();
        format.append("%1$20s |");
        int i = 1;
        Object[] docs = new Object[documentsMap.size() + 1];
        Object[] args = new Object[documentsMap.size() + 1];
        for (Map.Entry<String, Integer> entry : documentsMap.entrySet()) {
            docs[i] = entry.getValue();
            format.append(" %").append(++i).append("$5s |");
        }
        docs[0] = "word \\ document";
        out.format(format.append('\n').toString(), docs);
        for (Map.Entry<String, Set<Integer>> entry : index.entrySet()) {
            args[0] = entry.getKey();
            for (int j = 1; j < documentsMap.size() + 1; ++j) {
                if (entry.getValue().contains(docs[j])) {
                    args[j] = 1;
                } else {
                    args[j] = 0;
                }
            }
            out.format(format.toString(), args);
        }
        out.close();
    }

    public static Dictionary readDictionaryFromFile(String file) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream oin = new ObjectInputStream(fis);
        Dictionary dictionary = (Dictionary) oin.readObject();
        return dictionary;
    }

}
