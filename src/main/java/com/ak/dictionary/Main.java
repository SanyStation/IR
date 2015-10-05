package com.ak.dictionary;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class Main {

    public static final String DATE_MASK = "dd.MM.yyyy_HH.mm.ss";
    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_OUT = "out";
    public static final int KILOBYTE = 1024;

    private static Dictionary dictionary = new Dictionary();

    public static void main(String[] args) throws IOException {
        if (args.length == 4) {
            String firstArg = args[0];
            if ("-d".equals(firstArg)) {
                System.out.println("Executing...");

                long lStartTime = System.nanoTime();
                int documents = new FileProcessor(args[1], dictionary).processFiles();
                long lEndTime = System.nanoTime();

                long difference = lEndTime - lStartTime;
                System.out.println("Elapsed time: " + difference / 1000000 + " ms");
                System.out.println("Document(s): " + documents);
                System.out.println(dictionary.toString());
            } else {
                System.out.println("The first argument should be '-d' (That means DIRECTORY)");
            }
            String thirdArg = args[2];
            if ("-d".equals(thirdArg)) {
                saveDictionary(dictionary, args[3]);
            } else {
                System.out.println("The third argument should be '-d' (That means DIRECTORY)");
            }
        } else {
            System.out.println("There should be passed 4 arguments into the application (For example, '-d c:/Documents -d c:/Output') !!! Without slash in the end. The application will be closed.");
        }
    }

    private static void saveDictionary(Dictionary dictionary, String directory) throws IOException {
        directory = normalizeAddress(directory);
        serializeDictionary(dictionary, directory);
        saveToTextFileDictionary(dictionary, directory);
    }

    private static void serializeDictionary(Dictionary dictionary, String directory) throws IOException {
        String fileName = generateFileName(directory, EXTENSION_OUT);
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dictionary);
        oos.flush();
        oos.close();
    }

    public static void saveToTextFileDictionary(Dictionary dictionary, String directory) throws IOException {
        String fileName = generateFileName(directory, EXTENSION_TXT);
        Writer fileWriter = new BufferedWriter(new FileWriter(fileName, false));
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
        System.out.println("Matrix: ");
        System.out.println(dictionary.getIncedenceMatrix());
    }

    private static String generateFileName(String directory, String extension) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_MASK);
        String formattedDate = sdf.format(date);
        StringBuilder sb = new StringBuilder(directory);
        sb.append("/dictionary_").append(formattedDate).append(".").append(extension);
        return sb.toString();
    }

    private static String normalizeAddress(String address) {
        if (address.contains("/")) return address.replace("/", File.separator);
        if (address.contains("\\")) return address.replace("\\", File.separator);
        return address;

    }

}
