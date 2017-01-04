package com.oodrive.omnikles.depotclient;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

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
		Security.addProvider(new BouncyCastleProvider());
//		SunMSCAPI providerMSCAPI = new SunMSCAPI();
//		Security.addProvider(providerMSCAPI);
		try {
			if(isLinux){
				ks = KeyStore.getInstance("jks");
				File test=new File(System.getProperty("java.home")+ "/lib/security/cacerts");
				FileInputStream inks = new FileInputStream(test);
				ks.load(inks, "changeit".toCharArray());
			}
			if(isWindows){
				ks = KeyStore.getInstance("Windows-MY");
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
			Key keytmp = null;
			try {
				keytmp = keyStore.getKey(keyAlias, password);
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
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
				java.security.cert.Certificate[] certificateChain;
				certificateChain = keyStore.getCertificateChain(keyAlias);
				if(certificateChain != null && certificateChain.length > 0) {
					X509Certificate signerCertificate = (X509Certificate) certificateChain[0];
					keyPairs.add(new KeyPair(signerCertificate, (PrivateKey) keytmp, keyAlias));
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
		System.out.println("Alias : " + alias);
		System.out.println("Password : " + password);
		try {
			KeyStore ks = getKeyStore();
			PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
			ks.getCertificate(alias);
			java.security.cert.Certificate[] certificateChain = null;
			try {
				certificateChain = ks.getCertificateChain(alias);
			} catch (KeyStoreException e) {
				e.printStackTrace();
			}
			X509Certificate signerCertificate = (X509Certificate) certificateChain[0];
			return new KeyPair(signerCertificate, privateKey, alias);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
