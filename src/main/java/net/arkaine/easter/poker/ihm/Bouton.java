package net.arkaine.easter.poker.ihm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Bouton extends JButton {

	private static final long serialVersionUID = -8379297241317864057L;
	private Color couleurBas = Color.WHITE;
	private Color couleurNormale=Color.WHITE;
	private final Font actif = new Font("Calibri", Font.PLAIN, 17);
	private final Font inactif = new Font("Calibri", Font.PLAIN, 16);
	private Font font=inactif;

	public Bouton(String titre){
		super(titre);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(isEnabled()){
					setBackground(new Color(242, 1, 46));
				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if(isEnabled()){
					setBackground(Color.WHITE);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
		});
	}

	@Override
	public void setEnabled(boolean actif){
		super.setEnabled(actif);
		if(actif){
			setForeground(Color.WHITE);
			couleurBas = new Color(23, 0, 114);
			setBackground(new Color(242, 1, 46));
			font=this.actif;
		}
		else {
			setForeground(Color.BLACK);
			setBackground(Color.BLACK);
			couleurBas = Color.WHITE;
			font=inactif;
		}
	}

	@Override
	public void paintComponent(Graphics g){
		Paint paint;
		Graphics2D g2d;
		if (g instanceof Graphics2D) {
			g2d = (Graphics2D) g;
		}
		else {
			System.out.println("Error");
			return;
		}
		paint = new GradientPaint(0,0, couleurBas, getWidth(), 70, getBackground());
		g2d.setPaint(paint);
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		g2d.setColor(couleurNormale);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
		g.setColor(getForeground());
		g.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int x = (this.getWidth() - fm.stringWidth(getText())) / 2;
		int y = (fm.getAscent() + (this.getHeight() -
				(fm.getAscent() + fm.getDescent())) / 2);
		g2d.drawString(getText(), x, y);
	}
	
	@Override
	public void paintBorder(Graphics g){
		
	}
}
