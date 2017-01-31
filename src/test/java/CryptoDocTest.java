import com.oodrive.omnikles.depotclient.CryptoDoc;
import com.oodrive.omnikles.depotclient.pojo.CryptoDocConfiguration;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.swing.window.MainWindow;

import javax.swing.*;
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

    private static MainWindow mainWindow ;
    private static TestWindow testWindow ;
    private static CryptoService cs;

    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
        System.out.println("WebStart CryptoDoc !");
        CryptoDoc cryptoDoc = new CryptoDoc();
        System.out.println( cryptoDoc.getAppVersion());
        System.out.println(System.getProperty("user.home"));
        CryptoDocConfiguration.initParameters(args);
        mainWindow = new MainWindow();
        testWindow = new TestWindow();
        cs = new CryptoService();

        testWindow.setVisible(true);
    }

    public static void initDebug(HashMap<String, String> parameters) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        mainWindow.init();
        File f = depotTest();
        cs.decryptP12(f, "test.p12", "ok".toCharArray());
    }

    public static File depotTest() throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {

        List<KeyPair> certificats = cs.getKeyPairList("ok".toCharArray(), new File("test.p12"));
        String selectFile = fileChooser("Selectioner le fichier à déposer.", mainWindow);
        System.out.println(selectFile);
        String result = certificats.get(0).getX509CertificateB64();
        System.out.println(result);
        System.out.println("PK : "+certificats.get(0).getPkB64());
        List<String > certs = new ArrayList<>();
        certs.add(result);
        return cs.crypteByCertificats(new File(selectFile), certs);
    }

    public static String fileChooser(String title, MainWindow mainWindow) {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(System.getenv("HOME"));
        c.setDialogTitle(title);
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(mainWindow);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            return dir + File.separatorChar + filename;
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            filename = null ;
            dir = null;
        }
        return null;
    }
}
