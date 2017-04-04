package com.oodrive.omnikles.cryptodoc.service;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.swing.component.AnimatedProgressBar;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by olivier on 18/01/17.
 */
public class ZipService {

    public AnimatedProgressBar getLabel() {
        return progressBar;
    }

    private AnimatedProgressBar progressBar = null;

    private int jobNumber = 0;
    private int maxPercent = 100 ;

    public int getMaxPercent() {
        return maxPercent;
    }

    public void setMaxPercent(int maxPercent) {
        this.maxPercent = maxPercent;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public void setProgressBar(AnimatedProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void unzip(String zipFilePath, String destDir, boolean rewrite) {
        File dir = new File(destDir);
        if(rewrite)
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        // create output directory if it doesn't exist
        if(!dir.exists())
            dir.mkdirs();
        FileInputStream fis;
        long totalSizeZip = (new File(zipFilePath)).length();
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            int size = 0;
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                    if(progressBar != null){
                        size += buffer.length;
                        progressBar.setActualIcon(Math.round((size*100)/totalSizeZip));
                    }
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFilesToNewZip(File zip,  List<File> files ){
        try {
            FileOutputStream fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[2048];
            long size = 0;
            int percentMem = -1;
            for (File file: files) {
                File currentFile = file;
                if (!currentFile.isDirectory()) {
                    ZipEntry entry = new ZipEntry(currentFile.getName());
                    FileInputStream fis = new FileInputStream(currentFile);
                    zos.putNextEntry(entry);
                    int read = 0;
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, read);
                        if(progressBar != null){
                            size += buffer.length;
                            int percent = Math.round((size*maxPercent)/Configuration.totalSizeFiles);
                            if(percentMem != percent){
                                progressBar.setActualIcon(percent + (maxPercent*jobNumber));
                                percentMem = percent;
                                progressBar.setText(CryptoDoc.textProperties.getProperty("depot.page4.sending") +
                                        (percent + (maxPercent*jobNumber)) + "%");
                            }
                        }
                    }
                    zos.closeEntry();
                    fis.close();
                }
            }
            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found : " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getContentFile(ZipFile zip, String fileName) throws IOException {
        System.out.println("Methode getContentFile");
        System.out.println("Arguments :" + zip.getName() + " | " + fileName);
        if(zip != null && zip.size() > 0) {
            System.out.println("Zip size : " + zip.size());
            ZipEntry entry = zip.getEntry(fileName);
            InputStream stream = zip.getInputStream(entry);
            return IOUtils.toByteArray(stream);
        }
        return null;
    }

    public static ZipService getInstance() {
        if (null == instance) {
            getUniqueInstance__();
        }
        return instance;
    }
    synchronized private static void getUniqueInstance__() {
        instance =  new ZipService();
    }
    private ZipService() {
    }

    private static ZipService instance;
}
