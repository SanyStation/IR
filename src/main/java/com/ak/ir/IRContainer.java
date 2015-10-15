package com.ak.ir;

import com.ak.ir.dictionary.Dictionary;
import com.ak.ir.index.IncidenceMatrix;
import com.ak.ir.index.InvertedIndex;
import com.ak.ir.utils.IOUtils;
import com.ak.ir.utils.IRUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Created by olko06141 on 15.10.2015.
 */
public class IRContainer {

    private IncidenceMatrix incidenceMatrix;
    private Dictionary dictionary = new Dictionary();
    private DocumentsMap documentsMap = new DocumentsMap();
    private InvertedIndex invertedIndex = new InvertedIndex();

    public static void main(String[] args) throws IOException {
        IRContainer irFacade = new IRContainer();
        if (args.length == 4) {
            String firstArg = args[0];
            if ("-d".equals(firstArg)) {
                System.out.println("Executing...");
                long lStartTime = System.nanoTime();
                irFacade.buildIRObjects(args[1]);
                long lEndTime = System.nanoTime();
                long difference = lEndTime - lStartTime;
                System.out.println("Elapsed time: " + difference / IOUtils.MILLISECONDS + " ms");
            } else {
                System.out.println("The first argument should be '-d' (That means DIRECTORY)");
            }
            String thirdArg = args[2];
            if ("-d".equals(thirdArg)) irFacade.saveIRObjects(args[3]);
            else System.out.println("The third argument should be '-d' (That means DIRECTORY)");
        } else {
            System.out.println("There should be passed 4 arguments into the application (for example, '-d c:/Documents -d c:/Output') !!! Without slash in the end. The application will be closed.");
        }
    }

    public void buildIncidenceMatrix() {
        incidenceMatrix = new IncidenceMatrix(invertedIndex, documentsMap);
    }

    private void buildDocumentsMap(String directory) throws IOException {
        File dir = new File(directory);
        if (!dir.isDirectory()) throw new RuntimeException("There is no such directory '" + dir + "'");
        for (File file : dir.listFiles()) documentsMap.addDocument(file.getCanonicalPath());
    }

    public void buildIRObjects(String directory) throws IOException {
        buildDocumentsMap(directory);
        for (Map.Entry<String, Integer> entry : documentsMap.getDocumentsMap().entrySet()) {
            String fileName = entry.getKey();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line;
            while ((line = inputStream.readLine()) != null) {
                for (String symbol : IRUtils.EXTRA_SYMBOLS) line = line.replace(symbol, IRUtils.SPACE_SYMBOL);
                Collection<String> collection = new ArrayList(Arrays.asList(line.split(IRUtils.SPACE_SYMBOL)));
                collection.stream().forEach(IRUtils::normalize);
                dictionary.addArrayOfWords(collection);
            }
            inputStream.close();
        }
    }

    public void saveIRObjects(String destinationDirectory) throws IOException {
        documentsMap.save(destinationDirectory);
        dictionary.save(destinationDirectory);

    }

}
