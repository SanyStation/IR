package com.ak.ir;

import com.ak.ir.dictionary.Dictionary;
import com.ak.ir.index.IncidenceMatrix;
import com.ak.ir.index.InvertedIndex;
import com.ak.ir.index.ZoneIndex;
import com.ak.ir.utils.IOUtils;

import java.io.*;

/**
 * Created by olko06141 on 15.10.2015.
 */
public class IRContainer {

    private ZoneIndex zoneIndex = new ZoneIndex();
    private Dictionary dictionary = new Dictionary();
    private DocumentsMap documentsMap = new DocumentsMap();
    private InvertedIndex invertedIndex = new InvertedIndex();
    private IncidenceMatrix incidenceMatrix = new IncidenceMatrix();

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

    private void buildDocumentsMap(String directory) throws IOException {
        File dir = new File(directory);
        if (!dir.isDirectory()) throw new RuntimeException("There is no such directory '" + dir + "'");
        for (File file : dir.listFiles()) documentsMap.addDocument(file.getCanonicalPath());
    }

    public void buildIRObjects(String directory) throws IOException {
        buildDocumentsMap(directory);
        zoneIndex.buildIRObject(documentsMap);
//        dictionary.buildIRObject(documentsMap);
//        invertedIndex.buildIRObject(documentsMap);
//        incidenceMatrix.buildIRObject(documentsMap);
    }

    public void saveIRObjects(String destinationDirectory) throws IOException {
        documentsMap.save(destinationDirectory);
        zoneIndex.save(destinationDirectory);
//        dictionary.save(destinationDirectory);
//        invertedIndex.save(destinationDirectory);
//        incidenceMatrix.save(destinationDirectory);
    }

}
