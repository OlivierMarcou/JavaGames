package com.oodrive.omnikles.cryptodoc.swing.component.template.easter.demin;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//dialogue pour personnaliser la taille
public class Personaliser extends JDialog implements ActionListener {
  private JPanel panel1 = new JPanel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JTextField h = new JTextField();
  private JTextField l = new JTextField();
  private JTextField m = new JTextField();
  private Border border1;
  private JButton ok = new JButton();
  private JButton cancel = new JButton();

  int H, L, M;
  Demineur demin;

  public Personaliser(Frame frame, String title, boolean modal, int hauteur, int largeur, int mines) {
    super(frame, title, modal);
    H=hauteur;
    L=largeur;
    M=mines;
    demin=(Demineur) frame;
    try {
      jbInit();
      pack();//ajuste la dimension de la fenetre automatiquement
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public Personaliser() {
    this(null, "", true, 16, 30, 99);
  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(124, 124, 124),new Color(178, 178, 178));
    panel1.setLayout(gridBagLayout1);
    jLabel1.setMaximumSize(new Dimension(70, 30));
    jLabel1.setMinimumSize(new Dimension(70, 30));
    jLabel1.setPreferredSize(new Dimension(70, 30));
    jLabel1.setText("Hauteur :");
    jLabel2.setMaximumSize(new Dimension(70, 30));
    jLabel2.setMinimumSize(new Dimension(70, 30));
    jLabel2.setPreferredSize(new Dimension(70, 30));
    jLabel2.setToolTipText("");
    jLabel2.setText("Largeur :");
    jLabel3.setMaximumSize(new Dimension(70, 30));
    jLabel3.setMinimumSize(new Dimension(70, 30));
    jLabel3.setPreferredSize(new Dimension(70, 30));
    jLabel3.setText("Mines :");
    h.setBorder(border1);
    h.setMinimumSize(new Dimension(40, 21));
    h.setPreferredSize(new Dimension(40, 21));
    h.setText(""+H);
    l.setBorder(border1);
    l.setMinimumSize(new Dimension(40, 21));
    l.setPreferredSize(new Dimension(40, 21));
    l.setText(""+L);
    m.setBorder(border1);
    m.setMinimumSize(new Dimension(40, 21));
    m.setPreferredSize(new Dimension(40, 21));
    m.setText(""+M);
    ok.setMaximumSize(new Dimension(70, 27));
    ok.setMinimumSize(new Dimension(70, 27));
    ok.setPreferredSize(new Dimension(70, 27));
    ok.setMnemonic('O');
    ok.setText("OK");
    ok.addActionListener(this);
    cancel.setMaximumSize(new Dimension(70, 27));
    cancel.setMinimumSize(new Dimension(70, 27));
    cancel.setPreferredSize(new Dimension(70, 27));
    cancel.setMargin(new Insets(2, 10, 2, 10));
    cancel.setMnemonic('A');
    cancel.setText("Annuler");
    cancel.addActionListener(this);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    getContentPane().add(panel1);
    panel1.add(jLabel1,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(jLabel2,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(jLabel3,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(h,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
    panel1.add(l,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
    panel1.add(m,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 20), 0, 0));
    panel1.add(ok,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    panel1.add(cancel,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
  }
  public void actionPerformed(ActionEvent e) {
    if (e.getSource()==ok) {
      try {
        int nH=Integer.parseInt(h.getText());
        //restrictions sur les dimensions et le nombre de mines
        if (nH<1) nH=1;
        if (nH>40) nH=40;
        int nL=Integer.parseInt(l.getText());
        if (nL<1) nL=1;
        if (nL>40) nL=40;
        int nMines=Integer.parseInt(m.getText());
        if (nMines<0) nMines=0;
        if (nMines>nL*nH) nMines=nL*nH;
        demin.dispose();// on detruit le demineur en cours
        System.gc();
        Demineur demineur = new Demineur(nH,nL,nMines,4);//on cree le nouveau
      }
      catch (Exception exc) {//retournee par parseInt(String)
        this.setTitle("Valeurs incorrectes");
      }
    }
    if (e.getSource()==cancel) {
      this.setVisible(false);
    }
  }
}