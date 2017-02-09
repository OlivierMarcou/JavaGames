package com.oodrive.omnikles.depotclient.utils;

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
public class ZipUtils {

    public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
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

    public static void addFilesToNewZip(File zip,  List<File> files ){
        try {
            FileOutputStream   fos = new FileOutputStream(zip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[2048];
            for (File file: files) {
                File currentFile = file;
                if (!currentFile.isDirectory()) {
                    ZipEntry entry = new ZipEntry(currentFile.getName());
                    FileInputStream fis = new FileInputStream(currentFile);
                    zos.putNextEntry(entry);
                    int read = 0;
                    while ((read = fis.read(buffer)) != -1) {
                        zos.write(buffer, 0, read);
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

    public static byte[] getContentFile(ZipFile zip, String fileName) throws IOException {
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

}
