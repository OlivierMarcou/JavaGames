package com.oodrive.omnikles.cryptodoc.deposit.swing.component;

import javax.swing.*;
import java.io.File;

/**
 * Created by olivier on 07/02/17.
 */
public class FileLabel extends JLabel {

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    FileLabel(String title, File file){
        super(title);
        this.file = file;
    }
}
