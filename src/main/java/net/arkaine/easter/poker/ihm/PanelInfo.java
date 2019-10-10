package net.arkaine.easter.poker.ihm;

import net.arkaine.easter.poker.metier.combinaison.AbstractCombinaison;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;

public class PanelInfo extends JPanel{

	private static final long serialVersionUID = 2677739331449435679L;
	private static final PanelInfo instance = new PanelInfo();
	public static SimpleAttributeSet STYLE_NORMAL;
	public static SimpleAttributeSet STYLE_INFO;
	public static SimpleAttributeSet STYLE_TITRE;
	public static SimpleAttributeSet TITRE_CENTRE;
	private JTextPane stat;
	private JTextPane combinaison;

	private JTextPane textPane;

	private Image image;

	private PanelInfo(){
		creerStyles();
		try {
			image=ImageIO.read(getClass().getResourceAsStream(("/img/cartes.jpg")));
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 10, 5, 10);
		c.weightx=1;
		c.weighty=3;
		c.gridx=1;
		c.gridy=1;
		stat = new JTextPane();
		stat.setPreferredSize(new Dimension(200, 200));
		stat.setOpaque(false);
		afficherStat(0, 0, 0, 0);
		//afficherStat(0, 0, 0, 0);
		JScrollPane scrollStat = new JScrollPane(stat);
		scrollStat.setOpaque(false);
		scrollStat.getViewport().setOpaque(false);
		TitledBorder borderStat = BorderFactory.createTitledBorder("Statistiques");
		borderStat.setTitleColor(Color.WHITE);
		borderStat.setTitleFont(new Font("Courier new", Font.PLAIN, 12));
		scrollStat.setBorder(borderStat);
		add(scrollStat, c);
		c.gridy=2;
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane suivi = new JScrollPane(textPane);
		suivi.setPreferredSize(new Dimension(200, 300));
		suivi.setOpaque(false);
		textPane.setOpaque(false);
		suivi.getViewport().setOpaque(false);
		TitledBorder borderSuivi = BorderFactory.createTitledBorder("Détail des tours");
		borderSuivi.setTitleColor(Color.WHITE);
		borderSuivi.setTitleFont(new Font("Courier new", Font.PLAIN, 12));
		suivi.setBorder(borderSuivi);
		add(suivi, c);

		c.gridy=3;
		combinaison = new JTextPane();
		combinaison.setEditable(false);
		JScrollPane scrollCombinaison = new JScrollPane(combinaison);
		scrollCombinaison.setPreferredSize(new Dimension(200, 300));
		scrollCombinaison.setOpaque(false);
		combinaison.setOpaque(false);
		scrollCombinaison.getViewport().setOpaque(false);
		TitledBorder borderCombinaison = BorderFactory.createTitledBorder("Combinaison");
		borderCombinaison.setTitleColor(Color.WHITE);
		borderCombinaison.setTitleFont(new Font("Courier new", Font.PLAIN, 12));
		scrollCombinaison.setBorder(borderCombinaison);
		afficherCombinaison(null);
		add(scrollCombinaison, c);
	}

	private void creerStyles(){
		STYLE_TITRE = new SimpleAttributeSet();
		StyleConstants.setFontFamily(STYLE_TITRE, "Courier new");
		StyleConstants.setFontSize(STYLE_TITRE, 15);
		StyleConstants.setForeground(STYLE_TITRE, Color.WHITE);
		StyleConstants.setBold(STYLE_TITRE, true);

		TITRE_CENTRE = new SimpleAttributeSet();
		StyleConstants.setAlignment(TITRE_CENTRE, StyleConstants.ALIGN_CENTER);

		STYLE_NORMAL = new SimpleAttributeSet();
		StyleConstants.setFontFamily(STYLE_NORMAL, "Courier new");
		StyleConstants.setForeground(STYLE_NORMAL, Color.WHITE);
		StyleConstants.setFontSize(STYLE_NORMAL, 12);

		STYLE_INFO = new SimpleAttributeSet();
		StyleConstants.setFontFamily(STYLE_INFO, "Courier new");
		StyleConstants.setFontSize(STYLE_INFO, 12);
		StyleConstants.setForeground(STYLE_INFO, Color.WHITE);
	}

	public void afficherCombinaison(AbstractCombinaison c){
		try {
			StyledDocument doc=combinaison.getStyledDocument();
			doc.remove(0, doc.getLength());
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			if(c!=null){
				sb.append("Vous avez : ").append(c.getDescription());
			}
			else {
				sb.append("En attente de cartes");
			}
			doc.insertString(doc.getLength(), sb.toString()+"\n", STYLE_NORMAL);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void ajouterTexte(String texte, SimpleAttributeSet style){
		try {
			StyledDocument doc=textPane.getStyledDocument();
			doc.insertString(doc.getLength(), texte+"\n", style);
			textPane.setCaretPosition(doc.getLength());
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void afficherStat(int nbTours, int nbVicoire, int nbDefaites, int nbPasse){
		try {
			int pGagne=0;
			int pPerdu=0;
			int pPasse=0;
			StyledDocument doc=stat.getStyledDocument();
			doc.remove(0, doc.getLength());
			if(nbTours!=0){
				pGagne = (int) Math.floor(100*nbVicoire/nbTours);
				pPerdu = (int) Math.floor(100*nbDefaites/nbTours);
				pPasse = (int) Math.floor(100*nbPasse/nbTours);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("\n");
			sb.append(" Tours joués : ").append(nbTours).append("\n\n");
			sb.append(" Victoires   : ").append(nbVicoire).append("\n");
			sb.append("               ").append(pGagne).append("%\n\n");
			sb.append(" Défaites    : ").append(nbDefaites).append("\n");
			sb.append("               ").append(pPerdu).append("%\n\n");
			sb.append(" Passées     : ").append(nbPasse).append("\n");
			sb.append("               ").append(pPasse).append("%\n");
			doc.insertString(doc.getLength(), sb.toString(), STYLE_NORMAL);
		}
		catch (BadLocationException e) {
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

	/**
	 * @return the instance
	 */
	public static PanelInfo getInstance() {
		return instance;
	}

}
