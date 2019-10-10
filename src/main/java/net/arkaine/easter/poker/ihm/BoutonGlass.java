package net.arkaine.easter.poker.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BoutonGlass extends JButton {
	
    private static final long serialVersionUID = 1L;

    /**
     *  couleur de la bulle 
     */
    private Color couleur;

    /**
     * Couleur de la bulle active
     */
    private Color couleurActif;

    /**
     * Couleur de la bulle normale
     */
    private Color couleurNormal;

    private String texte;

    /**
     *
     * @param texte
     * @param couleurNormal
     * @param couleurActif
     */
    public BoutonGlass(String texte, Color couleurNormal, Color couleurActif) {
        this.couleur=couleurNormal;
        this.couleurNormal=couleurNormal;
        this.couleurActif=couleurActif;
        this.texte=texte;
        this.setOpaque(false);
        ajouterListener();
    }

    private void ajouterListener(){
        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {
                couleur=couleurNormal;
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                couleur=couleurActif;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
                
            }

        });
    }

    /**
     * Surcharge de paintComponent
     */
    public void paint(Graphics arg0) {
        int largeur=66;
        Graphics2D g2d = (Graphics2D) arg0;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(couleur);
        g2d.setStroke(new BasicStroke(3));
        GradientPaint gradient = new GradientPaint(largeur / 2, 4, couleur, (largeur) / 2, 4 + largeur, new Color(100,100,100,200));
        g2d.setPaint(gradient);
        g2d.fillOval(4, 4, largeur, largeur);
        gradient = new GradientPaint(largeur / 2, 4, Color.white, largeur / 2, 4 + largeur / 2, new Color(couleur.getRed(), couleur.getGreen(), couleur.getBlue(), 0));
        g2d.setPaint(gradient);
        g2d.fillOval(4+largeur/5, 4, 5*largeur/8, largeur/3);
        g2d.setColor(couleur);
        g2d.setFont(new Font("Garamond", Font.BOLD, 40));
        g2d.drawString(texte, 80, 45);
    }
}