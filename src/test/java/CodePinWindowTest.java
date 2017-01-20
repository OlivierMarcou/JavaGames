import com.oodrive.omnikles.depotclient.CryptoService;
import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.SslConnexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivateKey;

/**
 * Created by olivier on 04/01/17.
 */
public class CodePinWindowTest extends JDialog{

    private CryptoService cs = new CryptoService();

    CodePinWindowTest(File selectedFile, KeyPair selectedCertificat){

        setSize(300, 200);
        Container content = getContentPane();
                content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        content.add(new JLabel ( "Password : "), c);

        JTextArea txtPassword = new JTextArea ();
                txtPassword.setColumns(25);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        content.add(txtPassword, c);

        JButton go = new JButton("Valider");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        content.add(go, c);

        JButton annul = new JButton("Annuler");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        content.add(annul, c);

        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!txtPassword.getText().trim().isEmpty()){


                    SslConnexion ssl = new SslConnexion();
                    //Initialise la clé privé avec le code pin
                    PrivateKey pk = null;
                    try {
                        cs.getKeyPairWithPrivateKey(selectedCertificat.getAlias(), txtPassword.getText().trim());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    selectedCertificat.setPrivateKey(pk);
                    try {
                        cs.decryptWindows(selectedFile, selectedCertificat);
                        System.out.println("Decrypted ! ");
                        setVisible(false);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        setVisible(false);
                    }
                }
            }
        });
        annul.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        setAlwaysOnTop(true);
        setVisible(true);
    }

}
