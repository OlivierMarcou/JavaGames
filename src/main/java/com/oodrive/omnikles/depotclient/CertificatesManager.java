package com.oodrive.omnikles.depotclient;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CertificatesManager {

	public static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
	public static boolean isLinux = System.getProperty("os.name").toLowerCase().contains("linux");

	public KeyStore getKeyStore() throws Exception {
		KeyStore ks = null;
		Security.addProvider(new BouncyCastleProvider());
		try {
			if(isLinux){
				ks = KeyStore.getInstance("jks");
				ks.load(null, null);
//				ks = KeyStore.getInstance("jks");
//				ks.load(new FileInputStream(path), password);
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

	public List<KeyPair> loadKeyPairsFromKeystore(KeyStore keyStore) {
		List<KeyPair> keyPairs = null;
		try {
			keyPairs = new ArrayList<KeyPair>();
			Enumeration aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {
				String keyAlias = aliases.nextElement().toString();
				Key keytmp = keyStore.getKey(keyAlias, null);
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
