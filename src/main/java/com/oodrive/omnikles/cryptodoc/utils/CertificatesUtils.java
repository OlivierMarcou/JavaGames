package com.oodrive.omnikles.cryptodoc.utils;

import com.oodrive.omnikles.cryptodoc.CryptoDoc;
import com.oodrive.omnikles.cryptodoc.pojo.CertificateInformations;
import com.oodrive.omnikles.cryptodoc.pojo.Configuration;
import com.oodrive.omnikles.cryptodoc.pojo.KeyPair;
import sun.security.mscapi.SunMSCAPI;
import sun.security.pkcs11.SunPKCS11;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CertificatesUtils {

	public static Provider provider = null;

	public static X509Certificate getX509CertificateP12(String p12Filename, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
		KeyStore p12 = KeyStore.getInstance("pkcs12");
		p12.load(new FileInputStream(p12Filename), password.toCharArray());
		Enumeration e = p12.aliases();
		while (e.hasMoreElements()) {
			String alias = (String) e.nextElement();
			return (X509Certificate) p12.getCertificate(alias);
		}
		return null;
	}

	public static KeyStore getKeyStore() throws Exception {
		KeyStore ks = null;
		try {
			if (Configuration.isLinux) {
				List<String> paths = findFilesPaths("secmod.db", new File(System.getProperty("user.home")+"/.mozilla/firefox/"));
				String pkcs11ConfigSettings = "name=NSS\n" +
						"slot=2\n" +
						"library="+ CryptoDoc.contextProperties.getProperty("so.token.linux")+" \n" +
						"nssArgs=\"configdir='"+paths.get(0)+"' certPrefix='' keyPrefix='' secmod='secmod.db' flags=readOnly\"";

				byte[] pkcs11ConfigBytes = pkcs11ConfigSettings.getBytes();
				ByteArrayInputStream confStream = new ByteArrayInputStream(pkcs11ConfigBytes);
				provider = new SunPKCS11(confStream);
				Security.addProvider(provider);
				ks = KeyStore.getInstance("PKCS11", provider);
				ks.load(null, null);
			}
			if (Configuration.isWindows) {
				provider = new SunMSCAPI();
				Security.addProvider(provider);
				ks = KeyStore.getInstance(Configuration.WINDOWS_KEYSTORE, provider);
				ks.load(null, null);
			}
		} catch (Exception e) {
			System.out.println("Erreur: keystore invalide ou passphrase incorrect");
			return null;
		}
		return ks;
	}

	public static List<KeyPair> loadKeyPairsFromKeystore(KeyStore keyStore, char[] password) {
		List<com.oodrive.omnikles.cryptodoc.pojo.KeyPair> keyPairs = null;
		keyPairs = new ArrayList<>();
		Enumeration aliases = null;
		try {
			aliases = keyStore.aliases();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		while (aliases.hasMoreElements()) {
			String keyAlias = aliases.nextElement().toString();
			PrivateKey keytmp = null;
			try {
				keytmp = (PrivateKey) keyStore.getKey(keyAlias, password);
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
			} catch (UnrecoverableEntryException e) {
				e.printStackTrace();
			}
			if (keytmp != null) {
				System.out.println(
						"Get private key (" + keytmp +
								", encode : " + keytmp.getEncoded() +
								", \n Algorithm : " + keytmp.getAlgorithm() +
								", Format : " + keytmp.getFormat());
				System.out.println("key alias : " + keyAlias);
			}
			try {
				X509Certificate certificate = (X509Certificate) keyStore.getCertificate(keyAlias);
				if (certificate != null) {
					keyPairs.add(new com.oodrive.omnikles.cryptodoc.pojo.KeyPair(certificate, (PrivateKey) keytmp, keyAlias));
				}
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		}
		return keyPairs;
	}

	public static List<KeyPair> getKeyPairList(File p12File, char[] password)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		if (p12File.exists()) {
			KeyStore p12 = KeyStore.getInstance("pkcs12");
			p12.load(new FileInputStream(p12File), password);
			try {
				return loadKeyPairsFromKeystore(p12, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	public static List<CertificateInformations> getCertificatesX509(List<String> certificatesB64) {
		List<CertificateInformations> certificates = new ArrayList();
		for(String certificate:certificatesB64){
			certificates.add(new CertificateInformations(certificate));
		}
		return certificates;
	}

	public static List<com.oodrive.omnikles.cryptodoc.pojo.KeyPair> getInstalledCertificates() {
		List<com.oodrive.omnikles.cryptodoc.pojo.KeyPair> certificats = new ArrayList();
		try {
			KeyStore ks = getKeyStore();
			certificats = loadKeyPairsFromKeystore(ks, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return certificats;
	}

	public static KeyPair getKeyPairWithAlias(String alias, String password) throws Exception {
		KeyStore ks = getKeyStore();
		System.out.println("Alias : " + alias);
		System.out.println("Password : " + password);
		try {
			ks.load(null, null);
			java.util.Enumeration en = ks.aliases();
			while (en.hasMoreElements()) {
				String aliasKey = (String) en.nextElement();
				java.security.cert.Certificate c = ks.getCertificate(aliasKey);
				if (aliasKey.equals(alias)) {
					System.out.println("    Certificate : " + c.toString());
					PrivateKey key = (PrivateKey) ks.getKey(aliasKey, password.toCharArray());
					X509Certificate certificate = (X509Certificate) ks.getCertificate(aliasKey);
					return new KeyPair(certificate, key, aliasKey);
				}
			}
		} catch (Exception ioe) {
			System.err.println(ioe.getMessage());
		}
		return null;
	}

	private static List<String> findFilesPaths(String filename, File folder){
		List<String> paths = new ArrayList<>();
		return recursiveFolder(filename, folder, paths,1,0);
	}

	synchronized private static List<String> recursiveFolder(String filename, File folder, List<String> paths,Integer deep, Integer actualDeep) {
		for(File file:folder.listFiles()){
			if(file.exists() && file.isFile() && filename.equals(file.getName())){
				paths.add(file.getPath().substring(0, file.getPath().lastIndexOf(File.separatorChar)));
				return paths;
			}
			if(file.exists() && file.isDirectory()){
				if(deep > actualDeep){
					System.out.println(file.getAbsolutePath());
					actualDeep++;
					recursiveFolder(filename, file, paths, deep, actualDeep);
					actualDeep--;
				}
			}
		}
		return paths;
	}
}