package net.arkaine.easter.poker.ihm;

import net.arkaine.easter.poker.metier.Carte;

import javax.swing.*;
import java.awt.*;


public class ImageCarte extends JLabel {

	private static final long serialVersionUID = 8014948677862527403L;

	private Carte carte;
	private boolean afficher;

	public ImageCarte(Carte c, boolean afficher){
		this(afficher);
		setCarte(c);
	}

	public ImageCarte(boolean afficher){
		super();
		this.afficher=afficher;
	}

	/**
	 * @return the carte
	 */
	public Carte getCarte() {
		return carte;
	}

	/**
	 * @param carte the carte to set
	 */
	public void setCarte(Carte carte) {
		this.carte = carte;
		setIcon(null);
		if(carte!=null){
			ImageIcon source = null;
			if(afficher){
				source=new ImageIcon(getClass().getResource(("/img/"+carte.getValeur().getNom().toLowerCase()+"_"+carte.getCouleur().getNom().toLowerCase()+".png")));
			}
			else {
				source=new ImageIcon(getClass().getResource(("/img/verso.jpg")));
			}
			Image img = source.getImage() ;  
			Image newimg = img.getScaledInstance(80, 95, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newimg);
			setIcon(icon);
		}
		repaint();
	}

	public void set(Carte carte){
		this.carte = carte;
		repaint();
	}

	/**
	 * @param afficher the afficher to set
	 */
	public void setAfficher(boolean afficher) {
		this.afficher = afficher;
		setCarte(carte);
	}


}