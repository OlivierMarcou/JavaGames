package com.oodrive.omnikles.cryptodoc.service;


import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import com.oodrive.omnikles.cryptodoc.utils.InitDLL;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.xml.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Applet qui realise les operations d'ouverture des plis
 */
public class Decrypt {

    private static String 	pathouverture;



	public static byte[] decryptMessage(byte[] crypted, RSAPrivateKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    return cipher.doFinal(crypted);
	}


    /**
     * openEnveloppe : ouverture des enveloppes
     *
     * @param enveloppe
     *            File
     * @param unzipDir
     *            String
     * @return String[]
     */
    public static String[] openEnveloppe( File enveloppe, String unzipDir,  KeyPair keyPair) {
        System.out.println("openEnveloppe");
        System.out.println("unzipDir " + unzipDir);




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
            String envpCryptUnzippedFile = "";
            while ((e = zin.getNextEntry()) != null) {
                // unzip(zin, e.getName());
                String entryname = e.getName();
                // System.out.println(entryname);
                int index = entryname.lastIndexOf("/");
                if (index > 0) {
                    entryname = entryname.substring(index + 1);
                }
                if (entryname.indexOf(".key.p7m") > 0) {
                    // alors c'est le fichier de cle
                    keyUnzippedFile = unzipDir+ entryname;// unzipDir + File.separator +
                    // entryname;
                    out = new FileOutputStream(keyUnzippedFile);
                } else {
                    // alors c'est l'enveloppe cryptee
                    envpCryptUnzippedFile = unzipDir+ entryname; // unzipDir +
                    // File.separator
                    // + entryname;
                    out = new FileOutputStream(envpCryptUnzippedFile);
                }
                byte[] b = new byte[512];
                int len = 0;
                while ((len = zin.read(b)) != -1) {
                    // System.out.println("1");
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
                System.out.println("File is too large to process");
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
            // ok on accepte le decryptage
            boolean ok4decrypt = true;
            // if (!test) {
            ok4decrypt = false;
            // il y a eu un reel cryptage donc il faut parser le fichier de cle
            // et le lire
            String contenuFichier = new String(bytes);
            int indexCert = 0;
            boolean loop = true;// le parametre pour savoir si il faut continuer
            // e chercher
            boolean hascert2decrypt = false; // le parametre pour savoir si
            // il y a du cryptage
            // donc des infos pour decrypter
            String certAanalyser = "";
            while (loop) {
                indexCert = contenuFichier.indexOf("<ds:X509Certificate>");
                if (indexCert > 0) {
                    hascert2decrypt = true;
                    indexCert = indexCert + 20;
                    certAanalyser = contenuFichier
                            .substring(indexCert, contenuFichier.indexOf("</ds:X509Certificate>"));
                    // "\n"
                    certAanalyser = certAanalyser.replaceAll("\n", "");
                    details[7] = certAanalyser; // il est change tant qu'on
                    // n'est pas sur le bon
//                    try {
//                        byte[] cert2test = Base64.decode(certAanalyser.getBytes());
//                    } catch (Base64DecodingException e1) {
//                        e1.printStackTrace();
//                    }
//                    AbstractCertificateHandler h = this.setCertificate(cert2test);


                    System.out.println("loop - recherche de certificat - Fin de la boucle : la cle privee a ete trouvee ");
                    int index2 = contenuFichier.indexOf("<ds:EncryptedKey>") + 17;
                    String encryptedKey = contenuFichier.substring(index2, contenuFichier
                            .indexOf("</ds:EncryptedKey>"));
                    try {
                        InitDLL dll = new InitDLL();
                        encryptedKey = encryptedKey.replaceAll("\n", "");
                        System.out.println("Valeur de la cle symetrique cryptee = " + encryptedKey + "\n\n");
                       // byte[] bytecrypted = Base64.decode(encryptedKey.getBytes());

                        byte[] bytecrypted = dll.mscproviderDLL.cryptMessage("holey !".getBytes(), keyPair.getPrivateKey().getEncoded());
                        System.out.println(" ------------------- Crypted byte ? => " + bytecrypted);
                        byte[] bytedecrypted = dll.mscproviderDLL.decryptMessage(bytecrypted, keyPair.getPrivateKey().getEncoded());
                        System.out.println("error code : " + dll.mscproviderDLL.getLastErrorCode());
                        if ((bytedecrypted != null) && (bytedecrypted.length > 0)) {
                            bytes = bytedecrypted;
                            System.out.println("cle symetrique decryptee - taille=" + bytes.length + "\n\n");
                            ok4decrypt = true;
                            loop = false;
                        } else {
                            System.out.println("La valeur de la cle symetrique decryptee est nulle\n\n");
                        }
                    } catch (Exception exc) {
                        System.out.println("Exception lors du decryptage=" + exc + "\n\n");
                        return null;
                    }

                    // FIXME : ea veut peut-etre dire que le fichier de base
                    // n'est pas crypte
					/*
					 * contenuFichier = contenuFichier.substring(
					 * contenuFichier.indexOf( "</ds:X509Certificate>")+20);
					 */
                    // </ds:EncryptedExchangeKey>
                    // FIXME : pour remonter le fichier au complet
                    contenuFichier = contenuFichier
                            .substring(contenuFichier.indexOf("</ds:EncryptedExchangeKey>") + 25);

                } else {
                    loop = false;
                    if (hascert2decrypt) {
                        System.out.println("Erreur : certificat pour dechiffrer non trouve sur le poste de travail\n\n");

                        return null; // ajout MBT du 20/09/2005
                    }
                }
            }
			/*
			 * } else { // ajout du 04/03 - decrypter les fichiers cryptes avec
			 * rien String contenuFichier = new String(bytes); if
			 * (contenuFichier.indexOf("<ds:EncryptedKey>") > 0) {
			 * //System.out.println("Utilisation cas 3 - ouverture d'une
			 * enveloppe cryptee avec rien"); int index2 =
			 * contenuFichier.indexOf("<ds:EncryptedKey>") + 17; String
			 * encryptedKey = contenuFichier.substring(index2,
			 * contenuFichier.indexOf( "</ds:EncryptedKey>")); encryptedKey =
			 * encryptedKey.replaceAll("\n", ""); bytes =
			 * Base64.decode(encryptedKey.getBytes()); }
			 *  }
			 */
            // ajout du 04/03
            if (!ok4decrypt) {
                // il y a eu un pb au decryptage de la cle -
                // peut etre que le cryptage n'etait pas active
                contenuFichier = new String(bytes);
                if (contenuFichier.indexOf("<ds:EncryptedKey>") > 0) {
                    // System.out.println("Utilisation cas 3 - ouverture d'une
                    // enveloppe cryptee avec rien");
                    int index2 = contenuFichier.indexOf("<ds:EncryptedKey>") + 17;
                    String encryptedKey = contenuFichier
                            .substring(index2, contenuFichier.indexOf("</ds:EncryptedKey>"));
                    encryptedKey = encryptedKey.replaceAll("\n", "");
                    try {
                        bytes = Base64.decode(encryptedKey.getBytes());
                    } catch (Base64DecodingException e1) {
                        e1.printStackTrace();
                    }
                }
                // sinon cela tentera d'ouvrir avec les bytes directement
            }
            // fin du check ok4decrypt
            // e present bytes represente ds tous les cas de figure la cle
            // symetrique decryptee
            SecretKeySpec skeySpec = new SecretKeySpec(bytes, "DESede");
            // JCOSecretKey bJCOSecretKey = new JCOSecretKey(bytes);
            // on commence la lecture du fichier crypte
            String tempunzipfile = keyUnzippedFile = pathouverture + File.separator + "TECHNIQUE" + File.separator
                    + 1 + "_temp.zip";// unzipDir + File.separator
            // + entryname;
            is = new FileInputStream(envpCryptUnzippedFile);
            out = new FileOutputStream(tempunzipfile);
            Cipher cipher = Cipher.getInstance("DESede");
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
            int taille = (int) (new File(tempunzipfile)).length();
            is = new FileInputStream(tempunzipfile);

            zin = new ZipInputStream(is);
            String currentUnzippedFile;
            details[6] = unzipDir;
            File filetmp = new File(unzipDir);
            if( !filetmp.mkdir())
                System.out.println("cannnot create "+unzipDir);
            // System.out.println("unzip : satge 1");
            while ((e = zin.getNextEntry()) != null) {
                // unzip(zin, e.getName());
                boolean iskeyfile = false;
                String entryname = e.getName();
                System.out.println("Ouverture fichier :" + entryname);
                nbdocs = nbdocs + 1;
                if (unzipDir.indexOf("36_") != -1) {
                    System.out.println(" 36 XXXXXXXXXXXXXXXXX");
                    String tmpzipdir = unzipDir.replaceFirst("36_", 1 + "_");
                    filetmp = new File(tmpzipdir);
                    filetmp.mkdir();
                    currentUnzippedFile = tmpzipdir + File.separator + entryname;
                } else if (unzipDir.indexOf("37_") != -1) {
                    System.out.println(" 37 XXXXXXXXXXXXXXXXX");
                    String tmpzipdir = unzipDir.replaceFirst("37_", 1 + "_");
                    filetmp = new File(tmpzipdir);
                    filetmp.mkdir();
                    currentUnzippedFile = tmpzipdir + File.separator + entryname;
                } else if (unzipDir.indexOf("38_") != -1) {
                    System.out.println(" 38 XXXXXXXXXXXXXXXXX");
                    String tmpzipdir = unzipDir.replaceFirst("38_", 1 + "_");
                    filetmp = new File(tmpzipdir);
                    filetmp.mkdir();
                    currentUnzippedFile = tmpzipdir + File.separator + entryname;
                } else {
                    currentUnzippedFile = unzipDir + File.separator + entryname;
                }
                if (entryname.indexOf("sig.xml") > 0) {
                    // il n'y a qu'un fichier de cle
                    // on considere que sa taille est negligeable pour le
                    // process
                    signatureFile = currentUnzippedFile;
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
                        // System.out.println("1");
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
                        // System.out.println("1");
                        out.write(b, 0, len);
                    }
                    out.close();
                }
                details[1] = details[1] + "<fichier_ouvert><nomfichier>" + entryname + "</nomfichier>";
                File f = new File(currentUnzippedFile);
                // on met les fichiers ouverts en readonly
                try {
                    f.setReadOnly();
                } catch (Exception exc) {
                }
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
                    System.out.println("File is too large to process");
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
                    System.out.println("Could not completely read file " + envpCryptUnzippedFile + "\n");
                    throw new IOException("Could not completely read file " + envpCryptUnzippedFile);
                }
                is.close();
                signatureXML = new String(bytes); // bytes.toString();
                // System.out.println("valeur du fichier de signature
                // :"+signatureXML);
                // e present, parser le fichier xml de signature
                // le certificat du signataire <ds:X509Certificate> et
                // </ds:X509Certificate>
                details[5] = signatureXML;
                // on distingue les cas
                if (signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">") > 0) {
                    indexCert = signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">") + 40;
                    if (indexCert > 0) {
                        System.out.println("valeur originale:"
                                + signatureXML
                                .substring(signatureXML.indexOf("<ds:X509Certificate type=\"base64Binary\">")));
                        details[2] = signatureXML.substring(indexCert, signatureXML.indexOf("</ds:X509Certificate>"));
                        // "\n"
                        System.out.println("nombre de \\n :" + details[2].indexOf("\n"));
                        System.out.println("valeur de la chaine :" + details[2]);
                    }
                } else if (signatureXML.indexOf("<ds:X509Certificate>") > 0) {
                    indexCert = signatureXML.indexOf("<ds:X509Certificate>") + 20;
                    if (indexCert > 0) {
                        System.out.println("valeur originale:"
                                + signatureXML.substring(signatureXML.indexOf("<ds:X509Certificate>")));
                        details[2] = signatureXML.substring(indexCert, signatureXML.indexOf("</ds:X509Certificate>"));
                        // "\n"
                        System.out.println("nombre de \\n :" + details[2].indexOf("\n"));
                        System.out.println("valeur de la chaene :" + details[2]);
                    }
                } else {
                    System.out.println("Erreur balise de signature X509certificate non trouvee");
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
            System.out.println("Erreur de fichier non trouve" + fnfe);
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur IO:" + ex);
            return null;
        }
        return details;
    }


}
