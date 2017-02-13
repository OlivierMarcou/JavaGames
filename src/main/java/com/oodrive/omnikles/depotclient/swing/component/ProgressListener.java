package com.oodrive.omnikles.depotclient.swing.component;

/**
 * Created by olivier on 09/02/17.
 */
public interface ProgressListener {
    void progress(long reste, long totalBytes, long transfered);
}
