package com.ak.ir.cluster;

import com.ak.ir.SavableReadable;

import java.io.PrintWriter;

/**
 * Created by sanystation on 05/11/15.
 */
public class Clasterer extends SavableReadable {

    public static final String FILE_TYPE = "claster";

    @Override
    protected void writeTo(PrintWriter printWriter) {

    }

    @Override
    protected String getFileType() {
        return FILE_TYPE;
    }
}
