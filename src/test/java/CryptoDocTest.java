import com.oodrive.omnikles.depotclient.CryptoService;
import com.oodrive.omnikles.depotclient.MainWindow;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by olivier on 16/09/16.
 */

public class CryptoDocTest {

    private static MainWindow mainWindow = new MainWindow();
    private static TestWindow testWindow = new TestWindow();
    private static CryptoService cs = new CryptoService();

    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        System.out.println("WebStart CryptoDoc !");
        System.out.println(System.getProperty("user.home"));
        HashMap<String, String> parameters = new HashMap<>();

        String[] keyValue = new String[2];
        for (String parameter: args ){
            int indexEqual = parameter.trim().indexOf("=");
            keyValue[0] = parameter.substring(0, indexEqual);
            keyValue[0].replaceFirst("-","");
            keyValue[1] = parameter.substring(indexEqual+1);
            System.out.println(keyValue[0] + " " + keyValue[1]);
            parameters.put(keyValue[0], keyValue[1]);
        }
        testWindow.setVisible(true);
//        initDebug(parameters);
//        mainWindow.setVisible(true);
    }

    public static void initDebug(HashMap<String, String> parameters) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
//        List<KeyPair> certificats = cs.getInstalledCertificats();
//        for(KeyPair certificat:certificats)
//            jCertificats.addItem(certificat);
        mainWindow.init(parameters);
        File f = depotTest();
        cs.decryptP12(f, "test.p12", "ok".toCharArray());
    }

    public static File depotTest() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {

        List<KeyPair> certificats = cs.getKeyPairList("ok".toCharArray(), new File("test.p12"));
        String selectFile = mainWindow.fileChooser();
        System.out.println(selectFile);
        String result = certificats.get(0).getX509CertificateB64();
        System.out.println(result);
        System.out.println("PK : "+certificats.get(0).getPkB64());
        List<String > certs = new ArrayList<>();
        certs.add(result);
        return cs.crypteByCertificats(new File(selectFile), certs);
    }
}
