package com.oodrive.omnikles.depotclient.swing.window;

import com.oodrive.omnikles.depotclient.service.CryptoService;
import com.oodrive.omnikles.depotclient.swing.action.ActionListenerDecrypt;
import com.oodrive.omnikles.depotclient.swing.action.ActionListenerHidden;
import com.oodrive.omnikles.depotclient.swing.component.PinCodeTextField;

import javax.swing.*;
import java.awt.*;

/**
 * Created by olivier on 04/01/17.
 */
public class CodePinWindow extends JDialog{

    private CryptoService cs = new CryptoService();
    private ActionListenerDecrypt actionDecrypt;
    public static String codePin = null;

    private PinCodeTextField txtPassword = new PinCodeTextField();

    public CodePinWindow(String urlCryptedFile, String sessionid, String filename ){

        setSize(300, 200);
        Container content = getContentPane();
                content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        content.add(new JLabel ( "Password : "), c);

        txtPassword.setColumns(25);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        content.add(txtPassword, c);

        JButton btnDecrypt = new JButton("Valider");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        content.add(btnDecrypt, c);

        JButton annul = new JButton("Annuler");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        content.add(annul, c);
        actionDecrypt = new ActionListenerDecrypt(urlCryptedFile, sessionid, filename);
        btnDecrypt.addActionListener(actionDecrypt);

        annul.addActionListener(new ActionListenerHidden());

        setAlwaysOnTop(true);
        setVisible(true);
    }
}
