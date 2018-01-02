package com.oodrive.omnikles.cryptodoc.service;


import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.SecretAndPublicKey;
import com.oodrive.omnikles.cryptodoc.utils.Logs;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Applet qui realise les operations d'ouverture des plis
 * TODO : OLD CODE SOURCE OK-MARCHES need refactoring
 */
public class DecryptOkMarchesService {

    private AESService aes = AESService.getInstance();

    private DecryptOkMarchesService(){}

    public String makeCr(String[] mydet) {
        String mycr = "";
        String mycr_ent = "<cr_entreprise>";
        mycr_ent = mycr_ent + "<entreprise>" + mydet[0] + "</entreprise>";
        mycr_ent = mycr_ent + "<fichiers>" + mydet[1] + "</fichiers>";
        mycr_ent = mycr_ent + "<certificat_ent>" + mydet[2] + "</certificat_ent>";
        mycr_ent = mycr_ent + "<nbdocs>" + mydet[3] + "</nbdocs>";

        mycr_ent = mycr_ent + "<signature>" + mydet[5] + "</signature>";
        mycr_ent = mycr_ent + "<repertoire>" + mydet[6] + "</repertoire>";

        mycr_ent = mycr_ent + "<certificat_ouverture>"+ mydet[7] + "</certificat_ouverture>";
        mycr_ent = mycr_ent + "</cr_entreprise>";
        mycr = mycr + mycr_ent;
        return mycr;
    }

    public String[] openEnveloppe(File enveloppe) {
        Logs.sp("openEnveloppe");

        // 1 dezipper clef symetrique et enveloppe
        String signatureFile = "";
        String signatureXML = "";
        int nbdocs = 0;
        String[] details = new String[8];
        details[0] = "";
        details[1] = ""; // liste des fichiers // FIXME : rajouter les hashs
        // de chacun des fichiers tels qu'e l'ouverture
        details[2] = ""; // certificat du signataire encode base64
        details[3] = ""; // nombre de documents
        details[4] = "valide"; // si les signatures sont ok ou non
        details[5] = ""; // fichier de signature
        details[6] = ""; // repertoire d'ouverture des plis
        details[7] = ""; // certificat ayant servi e l'ouverture des plis

        try {
            FileOutputStream out;
            InputStream in = new BufferedInputStream(new FileInputStream(enveloppe));
            ZipInputStream zin = new ZipInputStream(in);
            ZipEntry e;
            String keyUnzippedFile = "";
            String cryptedZipFileName = "";
            while ((e = zin.getNextEntry()) != null) {
                // unzip(zin, e.getName());
                String entryname = e.getName();
                // Logs.sp(entryname);
                int index = entryname.lastIndexOf("/");
                if (index > 0) {
                    entryname = entryname.substring(index + 1);
                }
                if (entryname.indexOf(".key.p7m") > 0) {
                    // alors c'est le fichier de cle
                    keyUnzippedFile = enveloppe.getParent() + File.separator + entryname;
                    // entryname;
                    out = new FileOutputStream(keyUnzippedFile);
                } else {
                    // alors c'est l'enveloppe cryptee
                    cryptedZipFileName = enveloppe.getParent() + File.separator + entryname;
                    // File.separator
                    // + entryname;
                    out = new FileOutputStream(cryptedZipFileName);
                }
                byte[] b = new byte[512];
                int len = 0;
                while ((len = zin.read(b)) != -1) {
                    // Logs.sp("1");
                    out.write(b, 0, len);
                }
                out.close();
            }
            zin.close();
            in.close();
			/*
			 * Ajout du 11/03 - transformer les fichiers en fichiers caches ce
			 * n'est pas possible car a priori system specific nuance : sous
			 * unix, il suffirait de renommer avec .xxxx
			 */
            // 2 decrypter l'enveloppe
            FileInputStream is = new FileInputStream(keyUnzippedFile);
            long length = (new File(keyUnzippedFile)).length();
            if (length > Integer.MAX_VALUE) {
                Logs.sp("File is too large to process");
                // return null;
            }
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];
            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {

                offset += numRead;
            }
            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + keyUnzippedFile);
            }
            is.close();
            SecretAndPublicKey secretAndPublicKey = new SecretAndPublicKey(new String(bytes));
            details[7] = secretAndPublicKey.getPublicCertificate();
            SecretKeySpec skeySpec = new SecretKeySpec(secretAndPublicKey.getSecretKey(), Configuration.CIPHER_KEY_ALGORITHME_MARCHES);

            File tempFolder = new File( enveloppe.getParent() + File.separator + "TECHNIQUE");
            tempFolder.mkdirs();
            String tempDecryptedZipFile = enveloppe.getParent() + File.separator + "TECHNIQUE" + File.separator
                    + 1 + "_temp.zip";

            is = new FileInputStream(cryptedZipFileName);
            out = new FileOutputStream(tempDecryptedZipFile);
            Cipher cipher = Cipher.getInstance(Configuration.CIPHER_KEY_ALGORITHME_MARCHES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) != -1) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();

            // 3 acceder au fichier XML de signatures : le lire
            int taille = (int) (new File(tempDecryptedZipFile)).length();
            is = new FileInputStream(tempDecryptedZipFile);

            zin = new ZipInputStream(is);
            details[6] = enveloppe.getParent() + File.separator + enveloppe.getName().replace(".zip.crypt", "");
            String tmpzipdir = enveloppe.getName()+ "_";
            if (enveloppe.getName().indexOf("36_") != -1) {
                Logs.sp(" 36 XXXXXXXXXXXXXXXXX");
                tmpzipdir = enveloppe.getName().replaceFirst("36_", 1 + "_");
            } else if (enveloppe.getName().indexOf("37_") != -1) {
                Logs.sp(" 37 XXXXXXXXXXXXXXXXX");
                tmpzipdir = enveloppe.getName().replaceFirst("37_", 1 + "_");
            } else if (enveloppe.getName().indexOf("38_") != -1) {
                Logs.sp(" 38 XXXXXXXXXXXXXXXXX");
                tmpzipdir = enveloppe.getName().replaceFirst("38_", 1 + "_");
            }
            File extractionFolder = new File(enveloppe.getParent() + File.separator + tmpzipdir);
            extractionFolder.mkdirs();

            // Logs.sp("unzip : satge 1");
            while ((e = zin.getNextEntry()) != null) {
                // unzip(zin, e.getName());
                boolean iskeyfile = false;
                String entryname = e.getName();
                Logs.sp("Ouverture fichier :" + entryname);
                nbdocs = nbdocs + 1;
                String currentUnzippedFile = extractionFolder + File.separator + entryname;
                if (entryname.indexOf("sig.xml") > 0) {
                    iskeyfile = true;
                }

                if ((!iskeyfile)) {
                    // ce n'est pas le fichier de cle et on utilise le
                    // javascript
                    out = new FileOutputStream(currentUnzippedFile);
                    byte[] b = new byte[1024];
                    int len = 0;
                    int compt = 0; // pour la barre de progression
                    int lastprog = 0; // pour la barre de progression
                    while ((len = zin.read(b)) != -1) {
                        // Logs.sp("1");
                        out.write(b, 0, len);
                        // barre de progression
                        compt = compt + len;
                        int prog = ((compt * 100 / taille));
                        // donc 32000 au lieu de 102400
                        if (lastprog < prog) {
                            lastprog = prog;
                        }
                    }
                    out.close();
                } else {
                    // recuperation du nom de l'entreprise
                    out = new FileOutputStream(currentUnzippedFile);
                    byte[] b = new byte[512];
                    int len = 0;
                    while ((len = zin.read(b)) != -1) {
                        // Logs.sp("1");
                        out.write(b, 0, len);
                    }
                    out.close();
                }
                details[1] = details[1] + "<fichier_ouvert><nomfichier>" + entryname + "</nomfichier>";
                File f = new File(currentUnzippedFile);
                details[1] = details[1] + "<taillefichier>" + f.length() + "</taillefichier>";
                details[1] = details[1] + "</fichier_ouvert>";
            } // fin du while de dezipp
            zin.close();
            is.close();
            // // lecture du fichier de signature

            File signature = new File(signatureFile);
            if (signature.exists()) {
                is = new FileInputStream(signatureFile);
                length = (new File(signatureFile)).length();
                if (length > Integer.MAX_VALUE) {
                    Logs.sp("File is too large to process");
                    // return null;
                }
                // Create the byte array to hold the data
                bytes = new byte[(int) length];
                // Read in the bytes
                offset = 0;
                numRead = 0;
                while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {

                    offset += numRead;
                }
                // Ensure all the bytes have been read in
                if (offset < bytes.length) {
                    Logs.sp("Could not completely read file " + cryptedZipFileName + "\n");
                    throw new IOException("Could not completely read file " + cryptedZipFileName);
                }
                is.close();
                signatureXML = new String(bytes); // bytes.toString();
                // Logs.sp("valeur du fichier de signature
                // :"+signatureXML);
                // e present, parser le fichier xml de signature
                // le certificat du signataire <ds:X509Certificate> et
                // </ds:X509Certificate>
                details[5] = signatureXML;
                // on distingue les cas
                if (signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">") > 0) {
                    int indexCert = signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">") + 40;
                    if (indexCert > 0) {
                        Logs.sp("valeur originale:"
                                + signatureXML
                                .substring(signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">")));
                        details[2] = signatureXML.substring(indexCert, signatureXML.indexOf("</ds:X509Certificate>"));
                        // "\n"
                        Logs.sp("nombre de \\n :" + details[2].indexOf("\n"));
                        Logs.sp("valeur de la chaine :" + details[2]);
                    }
                } else if (signatureXML.indexOf("<ds:X509Certificate>") > 0) {
                    int indexCert = signatureXML.indexOf("<ds:X509Certificate>") + 20;
                    if (indexCert > 0) {
                        Logs.sp("valeur originale:"
                                + signatureXML.substring(signatureXML.indexOf("<ds:X509Certificate>")));
                        details[2] = signatureXML.substring(indexCert, signatureXML.indexOf("</ds:X509Certificate>"));
                        // "\n"
                        Logs.sp("nombre de \\n :" + details[2].indexOf("\n"));
                        Logs.sp("valeur de la chaine :" + details[2]);
                    }
                } else {
                    Logs.sp("Erreur balise de signature X509certificate non trouvee");
                }
            }
            // nombre de documents dans l'enveloppe
            details[3] = String.valueOf(nbdocs);
            // Verifier les signatures des differents fichiers

        } catch (IllegalStateException ex4) {
            ex4.printStackTrace();
            details = null;
        } catch (InvalidKeyException ex3) {
            ex3.printStackTrace();
            details = null;
        } catch (NoSuchPaddingException ex2) {
            ex2.printStackTrace();
            details = null;
        } catch (NoSuchAlgorithmException ex2) {
            ex2.printStackTrace();
            details = null;
        }catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            Logs.sp("Erreur de fichier non trouve" + fnfe);
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logs.sp("Erreur IO:" + ex);
            return null;
        }
        return details;
    }


    public static DecryptOkMarchesService getInstance() {
        if (null == instance) {
            getUniqueInstance__();
        }
        return instance;
    }
    synchronized private static void getUniqueInstance__() {
        instance =  new DecryptOkMarchesService();
    }

    private static DecryptOkMarchesService instance;
}
