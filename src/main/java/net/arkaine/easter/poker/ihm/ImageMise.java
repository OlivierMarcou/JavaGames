package net.arkaine.easter.poker.ihm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImageMise extends JPanel {

	private static final long serialVersionUID = -426644965316694727L;

	private int valeurMise;
	private Image image;

	public ImageMise(int valeurMise){
		this.valeurMise=valeurMise;
		try {
			image=ImageIO.read(getClass().getResourceAsStream("/img/jeton.png"));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ImageMise(){
		this(-1);
	}

	@Override
	public void paintComponent(Graphics g){
		if(valeurMise>0){
				g.drawImage(image, 0, 0, 50, 50, null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 16));
			g.drawString(""+valeurMise, 50, 35);
		}
		else if (valeurMise==0){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 13));
			g.drawString("Parole", 0, 35);
		}
	}

	/**
	 * @return the valeurMise
	 */
	public int getValeurMise() {
		return Math.max(valeurMise, 0);
	}

	/**
	 * @param valeurMise the valeurMise to set
	 */
	public void setValeurMise(int valeurMise) {
		this.valeurMise = valeurMise;
		validate();
		repaint();
	}



}
