package com.ak.ir.dictionary;

import com.ak.ir.utils.IOUtils;

import java.io.IOException;

public class Main {

    private static Dictionary dictionary = new Dictionary();

    public static void main(String[] args) throws IOException {
        if (args.length == 4) {
            String firstArg = args[0];
            if ("-d".equals(firstArg)) {
                System.out.println("Executing...");
                long lStartTime = System.nanoTime();
//                int documents = new FileProcessor().processFiles(args[1], dictionary);
                long lEndTime = System.nanoTime();
                long difference = lEndTime - lStartTime;
                System.out.println("Elapsed time: " + difference / IOUtils.MILLISECONDS + " ms");
//                System.out.println("Document(s): " + documents);
                System.out.println(dictionary.toString());
            } else {
                System.out.println("The first argument should be '-d' (That means DIRECTORY)");
            }
            String thirdArg = args[2];
//            if ("-d".equals(thirdArg)) IOUtils.saveDictionary(dictionary, args[3]);
//            else System.out.println("The third argument should be '-d' (That means DIRECTORY)");
        } else {
            System.out.println("There should be passed 4 arguments into the application (for example, '-d c:/Documents -d c:/Output') !!! Without slash in the end. The application will be closed.");
        }
    }

}
