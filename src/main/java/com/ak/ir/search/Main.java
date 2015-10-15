package com.ak.ir.search;

import com.ak.ir.dictionary.Dictionary;

import java.io.IOException;

/**
 * Created by olko06141 on 6.10.2015.
 */
public class Main {

    private static Dictionary dictionary;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length == 4) {
            String firstArg = args[0];
            if ("-f".equals(firstArg)) {
                System.out.println("Unpacking...");
//                dictionary = IOUtils.readDictionaryFromFile(args[1]);
                System.out.println("Unpacking finished");
                System.out.println(dictionary.toString());
            } else
                System.out.println("The first argument should be '-f' (That means FILE. It's serialized dictionary)");
            String thirdArg = args[2];
//            DocumentSearcher documentSearcher = new DocumentSearcher(dictionary.getIncidenceMatrix());
            if ("-w".equals(thirdArg)) {
//                Set<String> documents = documentSearcher.findDocuments(args[3]);
                System.out.println("The words '" + args[3] + "' are found in the following documents: ");
//                if (documents.size() > 0) documents.forEach(System.out::println);
//                else System.out.println("There are not such documents");
            } else
                System.out.println("The third argument should be '-w' (That means WORD(s) that you want to find in the documents). For example, -w \"one two !three\"");
        } else {
            System.out.println("There should be passed 4 arguments into the application (For example, '-f c:/Documents/dictionary.out -w hello'). The application will be closed.");
        }
    }

}
