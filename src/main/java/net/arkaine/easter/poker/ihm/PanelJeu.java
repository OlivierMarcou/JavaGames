package net.arkaine.easter.poker.ihm;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PanelJeu extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3587145146884989743L;
	private Image image;
	
	public PanelJeu(){
		setPreferredSize(new Dimension(1000, 666));
		try {
			this.image = ImageIO.read(getClass().getResourceAsStream("/img/table.jpg"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(image!=null){
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		}
	}

}
