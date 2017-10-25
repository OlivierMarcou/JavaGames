package com.oodrive.omnikles.cryptodoc.thread;

import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;

import java.io.File;

/**
 * Created by olivier on 10/02/17.
 */
public class TestRunnable implements Runnable{

    private AnimatedProgressBar progressBar = null;
    public AnimatedProgressBar getProgressBar() {
        return progressBar;
    }
    public void setProgressBar(AnimatedProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public File file;

    public TestRunnable(){
    }

    @Override
    public void run() {
        SslConnexionService sc = SslConnexionService.getInstance();
        sc.setJobNumber(3);
        sc.setMaxPercent(25);
        if(file != null && file.exists())
            sc.sslUploadFileAndDownloadProof(file, "http://localhost/upload/upld.php", progressBar, null);
    }

}
