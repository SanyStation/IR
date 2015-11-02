package com.ak.ir;

import com.ak.ir.utils.IOUtils;

import java.io.*;

/**
 * Created by sanystation on 10/10/15.
 */
public abstract class SavableReadable implements Serializable {

    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_OUT = "ser";

    public void save(String destinationDirectory) throws IOException {
        destinationDirectory = IOUtils.normalizeAddress(destinationDirectory);
        File dir = new File(destinationDirectory);
        if (!dir.exists()) dir.mkdir();
        serialize(destinationDirectory);
        saveToTextFile(destinationDirectory);
    }

    private void serialize(String destinationDirectory) throws IOException {
        String fileName = IOUtils.generateFileName(destinationDirectory, getFileType(), EXTENSION_OUT);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    private void saveToTextFile(String destinationDirectory) throws IOException {
        String fileName = IOUtils.generateFileName(destinationDirectory, getFileType(), EXTENSION_TXT);
        PrintWriter out = new PrintWriter(new BufferedWriter(new java.io.FileWriter(fileName, false)));
        writeTo(out);
        out.close();
    }

    protected abstract void writeTo(PrintWriter printWriter);

    /**
     * This file type is an example.
     * Actually, for example, the file type has to be 'dictionary' or 'index' or another one in the children classes.
     *
     * @return String 'dictionary' or 'index' or another one
     */
    protected abstract String getFileType();

    public Object read(String file) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
        return ois.readObject();
    }
}
