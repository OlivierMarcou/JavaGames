package com.oodrive.omnikles.depotclient.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by olivier on 18/01/17.
 */
public class ZipUtils {


    public static void addFileToZip(File file, File zip) throws IOException {
        if(file != null && file.exists()) {
            if(zip != null && !zip.exists()) {
                FileOutputStream fos = new FileOutputStream(zip);
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry(file.getName().toString());
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
                zos.close();
            }else{
                File[] files = new File[1];
                files[0] = file;
                addFilesToZip(zip, files);
            }
        }
    }

    public static void addFilesToZip(File source, File[] files ){
        try{
            File tmpZip = File.createTempFile(source.getName(), null);
            tmpZip.delete();
            if(!source.renameTo(tmpZip)){
                throw new Exception("Could not make temp file (" + source.getName() + ")");
            }
            byte[] buffer = new byte[4096];
            ZipInputStream zin = new ZipInputStream(new FileInputStream(tmpZip));
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(source));
            for(int i = 0; i < files.length; i++){
                InputStream in = new FileInputStream(files[i]);
                out.putNextEntry(new ZipEntry(files[i].getName()));
                for(int read = in.read(buffer); read > -1; read = in.read(buffer)){
                    out.write(buffer, 0, read);
                }
                out.closeEntry();
                in.close();
            }
            for(ZipEntry ze = zin.getNextEntry(); ze != null; ze = zin.getNextEntry()){
                if(!zipEntryMatch(ze.getName(), files)){
                    out.putNextEntry(ze);
                    for(int read = zin.read(buffer); read > -1; read = zin.read(buffer)){
                        out.write(buffer, 0, read);
                    }
                    out.closeEntry();
                }
            }
            out.close();
            tmpZip.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean zipEntryMatch(String zeName, File[] files){
        for(int i = 0; i < files.length; i++){
            if((files[i].getName()).equals(zeName)){
                return true;
            }
        }
        return false;
    }
    public static byte[] getContentFile(ZipFile zip, String fileName) throws IOException {
        if(zip != null && zip.size() > 0) {
            ZipEntry entry = zip.getEntry(fileName);
            InputStream stream = zip.getInputStream(entry);
            return IOUtils.toByteArray(stream);
        }
        return null;
    }

}
