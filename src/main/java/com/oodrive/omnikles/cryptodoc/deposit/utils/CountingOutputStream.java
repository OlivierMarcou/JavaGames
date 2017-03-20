package com.oodrive.omnikles.cryptodoc.deposit.utils;

import com.oodrive.omnikles.cryptodoc.deposit.swing.component.ProgressListener;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by olivier on 09/02/17.
 */
public  class CountingOutputStream extends FilterOutputStream {
    private ProgressListener listener;
    private long transferred;
    private long totalBytes;

    public CountingOutputStream(
            OutputStream out, ProgressListener listener, long totalBytes) {
        super(out);
        this.listener = listener;
        transferred = 0;
        this.totalBytes = totalBytes;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        transferred += len;
        listener.progress(getReste(), totalBytes, transferred);
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        transferred++;
        listener.progress(getReste(), totalBytes, transferred);
    }

//    private float getCurrentProgress() {
//        return ((float) transferred / totalBytes) * 100;
//    }
    private long getReste() {
        return  totalBytes - transferred ;
    }
}