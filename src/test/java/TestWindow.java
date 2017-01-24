import com.oodrive.omnikles.depotclient.pojo.KeyPair;
import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.swing.component.CertificatsComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olivier on 08/12/16.
 */
public class TestWindow extends JFrame {

    private JLabel lblSelectCertificat = new JLabel("Selectionner votre certificat : ");

    private JButton btnSelectCryptedFile = new JButton("Parcourir");
    private JLabel lblSelectCryptedFile = new JLabel("Fichier à décrypter : ");
    private JTextField txtSelectCryptedFile = new JTextField("");
    private File selectedCryptedFile;
    private File selectedFile;
    private CertificatsComboBox listCertificats = new CertificatsComboBox();
    private JButton btnSelectedCertif = new JButton("Decrypter avec le certif selectionner");
    private CryptoService cs = new CryptoService();
    private JButton btnP12 = new JButton("Or Upload P12 File !");

    private JButton btnSelectFile = new JButton("Parcourir");
    private JLabel lblSelectFile = new JLabel("Fichier à crypter : ");
    private JTextField txtSelectFile = new JTextField("");

    private ActionListener decryptAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            JDialog message = new CodePinWindowTest(selectedCryptedFile);
        }
    };

    private ActionListener decryptActionP12 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            JDialog message = new PasswordP12WindowTest(selectedCryptedFile);
        }
    };

    private ActionListener cryptAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Start Decrypt !");
            List<String> listKeyPair = new ArrayList<>();
            listKeyPair.add(((KeyPair) listCertificats.getSelectedItem()).getX509CertificateB64());
            try {
                selectedCryptedFile = cs.crypteByCertificats(selectedFile, listKeyPair);
                txtSelectCryptedFile.setText(selectedCryptedFile.getName());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    };

    private ActionListener selectCryptedFile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Select crypted File !");
            selectedCryptedFile = new File(fileChooser("Fichier crypté"));
            txtSelectCryptedFile.setText(selectedCryptedFile.getName());
        }
    };

    private ActionListener selectFile = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Select crypted File !");
            selectedFile = new File(fileChooser("Fichier à crypter"));
            txtSelectFile.setText(selectedFile.getName());
        }
    };

    public TestWindow(){
        setSize(700, 400);

        Container content = getContentPane();
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("CryptoDoc");
        JPanel pnlCertificat = new JPanel();
        JPanel pnlCrypter = new JPanel();
        JPanel pnlDecrypter = new JPanel();

        pnlCertificat.setLayout(new GridBagLayout());
        pnlCrypter.setLayout(new GridBagLayout());
        pnlDecrypter.setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=0;        c.gridwidth=2;
        content.add(pnlCertificat, c);

        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=1;        c.gridwidth=1;
        content.add(pnlCrypter, c);

        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=1;        c.gridy=1;        c.gridwidth=1;
        content.add(pnlDecrypter, c);



        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=0;        c.gridwidth=2;
        pnlCertificat.add(listCertificats, c);



        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=0;        c.gridwidth=2;
        pnlDecrypter.add(lblSelectCryptedFile, c);
        txtSelectCryptedFile.setColumns(20);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=1;        c.gridwidth=1;
        pnlDecrypter.add(txtSelectCryptedFile, c);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=1;        c.gridy=1;        c.gridwidth=1;
        pnlDecrypter.add(btnSelectCryptedFile, c);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=2;        c.gridwidth=1;
        pnlDecrypter.add(btnSelectedCertif, c);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=1;        c.gridy=2;        c.gridwidth=1;
        pnlDecrypter.add(btnP12, c);


        txtSelectFile.setColumns(20);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=0;        c.gridwidth=1;
        pnlCrypter.add(lblSelectFile, c);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=1;        c.gridy=0;        c.gridwidth=1;
        pnlCrypter.add(txtSelectFile, c);
        c.fill = GridBagConstraints.HORIZONTAL;        c.gridx=0;        c.gridy=0;        c.gridwidth=2;
        pnlCrypter.add(btnSelectFile, c);


        btnP12.addActionListener(decryptActionP12);

        listCertificats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                myBox(evt);
            }
        });
        btnSelectedCertif.addActionListener(decryptAction);
        btnSelectCryptedFile.addActionListener(selectCryptedFile);
        btnSelectFile.addActionListener(selectFile);

        List<KeyPair> certificats = cs.getInstalledCertificats();
        for(KeyPair certificat:certificats){
            listCertificats.addItem(certificat);
            System.out.println(certificat.getPkB64());
        }
    }

    protected void myBox(ActionEvent evt) {
        if (listCertificats.getSelectedItem() != null) {
            System.out.println(listCertificats.getSelectedItem().toString());
        }
    }

    public String fileChooser(String title) {
        String filename = null;
        String dir = null;
        JFileChooser c = new JFileChooser(System.getenv("user.home"));
        c.setDialogTitle(title);
        c.setAcceptAllFileFilterUsed(false);
        int rVal = c.showOpenDialog(TestWindow.this);
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
