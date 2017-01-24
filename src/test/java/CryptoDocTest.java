import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.swing.window.MainWindow;
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
        CryptoDocConfiguration.initParameters(args);

        testWindow.setVisible(true);
    }

    public static void initDebug(HashMap<String, String> parameters) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        mainWindow.init();
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
