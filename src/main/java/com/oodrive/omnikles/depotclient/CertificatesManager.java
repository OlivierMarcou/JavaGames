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
		try {
			if(isLinux){
				ks = KeyStore.getInstance("jks");
				File test=new File(System.getProperty("java.home")+ "/lib/security/cacerts");
				FileInputStream inks = new FileInputStream(test);
				ks.load(inks, "changeit".toCharArray());
			}
			if(isWindows){
				ks = KeyStore.getInstance("Windows-MY", Security.getProvider("SunMSCAPI"));
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
		try {
			keyPairs = new ArrayList<KeyPair>();
			Enumeration aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				String keyAlias = aliases.nextElement().toString();
				Key keytmp = keyStore.getKey(keyAlias, password);
				if (keytmp instanceof PrivateKey) {
                    System.out.println("key alias : " + keyAlias);
					java.security.cert.Certificate[] certificateChain = keyStore
							.getCertificateChain(keyAlias);
					X509Certificate signerCertificate = (X509Certificate) certificateChain[0];
					keyPairs.add(new KeyPair(signerCertificate,
							(PrivateKey) keytmp));
				}
			}
		} catch (Exception e) {
			System.out.println("error " + e.getMessage());
		}
		return keyPairs;
	}
}
