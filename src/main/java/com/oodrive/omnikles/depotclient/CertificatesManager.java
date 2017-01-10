package com.oodrive.omnikles.depotclient;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.mscapi.SunMSCAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CertificatesManager {

	public static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
	public static boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

	public X509Certificate getX509CertificateP12(String p12Filename, String password) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
		KeyStore p12 = KeyStore.getInstance("pkcs12");
		p12.load(new FileInputStream(p12Filename), password.toCharArray());
		Enumeration e = p12.aliases();
		while (e.hasMoreElements()) {
			String alias = (String) e.nextElement();
			return (X509Certificate) p12.getCertificate(alias);
		}
		return null;
	}

	public KeyStore getKeyStore() throws Exception {
		KeyStore ks = null;
		try {
			if(isLinux){
				Security.addProvider(new BouncyCastleProvider());
				ks = KeyStore.getInstance("jks");
				File test=new File(System.getProperty("java.home")+ "/lib/security/cacerts");
				FileInputStream inks = new FileInputStream(test);
				ks.load(inks, "changeit".toCharArray());
			}
			if(isWindows){
				SunMSCAPI providerMSCAPI = new SunMSCAPI();
				Security.addProvider(providerMSCAPI);
				ks = KeyStore.getInstance("Windows-MY", providerMSCAPI);
				ks.load(null, null);
			}
		} catch (Exception e) {
			System.out.println("Erreur: keystore invalide ou passphrase incorrect");
			return null;
		}
		return ks;
	}

	public List<KeyPair> loadKeyPairsFromKeystore(KeyStore keyStore, char[] password) {
		List<KeyPair> keyPairs = null;
        keyPairs = new ArrayList<KeyPair>();
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

				KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
				keyStore.getEntry(keyAlias,  new KeyStore.PasswordProtection(password));
				keytmp = pkEntry.getPrivateKey();
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
				if(certificate != null) {
					keyPairs.add(new KeyPair(certificate, (PrivateKey) keytmp, keyAlias));
				}
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
		}
		return keyPairs;
	}

	public List<KeyPair> getKeyPairList(File p12File, char[] password)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
		if(p12File.exists()) {
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


	public List<KeyPair> getInstalledCertificats(){
		List<KeyPair> certificats =  new ArrayList<>();
		try {
			KeyStore ks = getKeyStore();
			certificats = loadKeyPairsFromKeystore(ks, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return certificats;
	}

	public KeyPair getKeyPairWithPrivateKey(String alias, String password){
		try {
		KeyStore ks = null;

		if(isLinux){
			Security.addProvider(new BouncyCastleProvider());
			ks = KeyStore.getInstance("jks");
			File test=new File(System.getProperty("java.home")+ "/lib/security/cacerts");
			FileInputStream inks = new FileInputStream(test);
			ks.load(inks, "changeit".toCharArray());
		}
		if(isWindows){
			SunMSCAPI providerMSCAPI = new SunMSCAPI();
			Security.addProvider(providerMSCAPI);
			ks = KeyStore.getInstance("Windows-MY", providerMSCAPI);
			ks.load(null, null);
		}

		ks.load(null, null);
			X509Certificate certificate = (X509Certificate)ks.getCertificate(alias);
			System.out.println("Alias: " + alias);
			System.out.println("  Subject: " + certificate.getSubjectDN());
			System.out.println("  Issued By: " + certificate.getIssuerDN());
			if (ks.isKeyEntry(alias)) {
				System.out.println("  Has private key ... ");
				KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry)
				ks.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
				PrivateKey myPrivateKey = pkEntry.getPrivateKey();
				System.out.println("encoded " + myPrivateKey.getEncoded());
				return new KeyPair(certificate, myPrivateKey, alias);
			}
		} catch (Exception ioe) {
			System.err.println(ioe.getMessage());
		}

//		System.out.println("Alias : " + alias);
//		System.out.println("Password : " + password);
//		try {
//			KeyStore ks = KeyStore.getInstance("Windows-MY");
//			ks.load(null, null) ;
//			java.util.Enumeration en = ks.aliases() ;
//
//			while (en.hasMoreElements()) {
//				String aliasKey = (String)en.nextElement() ;
//				java.security.cert.Certificate c = ks.getCertificate(aliasKey) ;
//
//				if (aliasKey.equals(alias) ) {
//					System.out.println("    Certificat : " + c.toString() ) ;
//					PrivateKey key = (PrivateKey)ks.getKey(aliasKey, password.toCharArray());
////					Certificate[] chain = ks.getCertificateChain(aliasKey);
//
//					X509Certificate certificate = (X509Certificate) ks.getCertificate(aliasKey);
//					return new KeyPair(certificate,key,aliasKey);
//				}
//			}
//
//		} catch (Exception ioe) {
//			System.err.println(ioe.getMessage());
//		}
		return null;
	}
}
