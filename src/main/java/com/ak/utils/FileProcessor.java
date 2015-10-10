package com.ak.utils;

import com.ak.dictionary.Dictionary;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by olko06141 on 20.9.2015.
 */
public class FileProcessor {

    public static final String[] EXTRA_SYMBOLS = new String[]{
            System.lineSeparator(), ",", "(", ")", "{", "}", "\"", "'", "“", ";",
            "[", "]", "\t", "—", "!", ".", "#", "&", "?", "|", "//", "--", "*", "\n", "\r"};
    public static final String SPACE_SYMBOL = " ";

    private File[] files;
    private Dictionary dictionary;

    public FileProcessor(String directory, Dictionary dictionary) {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            throw new RuntimeException("There is no such directory '" + dir + "'");
        }
        files = dir.listFiles();
        this.dictionary = dictionary;
    }

    public int processFiles() {
        int fileCounter = 0;
        for (File file : files) {
            ++fileCounter;
            try (FileInputStream inputStream = new FileInputStream(file)) {
                String fileInString = IOUtils.toString(inputStream);
                for (String symbol : EXTRA_SYMBOLS) {
                    fileInString = fileInString.replace(symbol, SPACE_SYMBOL);
                }
                String[] arrayOfWords = fileInString.split(SPACE_SYMBOL);
                dictionary.addArrayOfWords(arrayOfWords, file.getName());
            } catch (FileNotFoundException e) {
                System.out.println("File " + file.getName() + " wasn't found");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("There are some troubles with IO");
                e.printStackTrace();
            }
        }
        return fileCounter;
    }

}
