package net.arkaine.easter.poker.ihm;

import javax.swing.*;
import java.awt.*;

public class Dealer extends JPanel {
	
	private static final long serialVersionUID = 8713249573681651347L;

	@Override
	public void paintComponent(Graphics g){
		g.setColor(new Color(240, 240, 240));
		g.fillOval(0, 0, 45, 45);
		g.setColor(Color.BLACK);
		g.drawOval(0, 0, 45, 45);
		g.setFont(new Font("Vernada", Font.BOLD, 11));
		g.drawString("DEALER", 3, 27);
	}

}
