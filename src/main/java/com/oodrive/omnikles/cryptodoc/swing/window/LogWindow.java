package com.oodrive.omnikles.cryptodoc.swing.window;

import com.oodrive.omnikles.cryptodoc.deposit.service.SslConnexionService;
import com.oodrive.omnikles.cryptodoc.swing.component.CustomOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Created by olivier on 21/03/17.
 */
public class LogWindow extends JFrame {

    private JScrollPane scrollPane;
    private JTextArea textArea = new JTextArea(10, 10);
    private PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
    private SslConnexionService ssl = new SslConnexionService();

    private int charNumber = 0;
    public LogWindow() {
        textArea.setLineWrap(true);
        setSize(600,600);
        setMinimumSize( new Dimension(600,600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        System.setOut(printStream);
        System.setErr(printStream);
        setLayout(new BorderLayout());
        add(textArea, BorderLayout.CENTER);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(20, 380));

        add(scrollPane, BorderLayout.CENTER);
        pack();
        setVisible(true);
        System.out.print("Logs actifs");
    }
}
