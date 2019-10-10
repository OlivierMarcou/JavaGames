package net.arkaine.easter.poker.ihm;

import net.arkaine.easter.poker.ihm.listeners.ListenerMiser;
import net.arkaine.easter.poker.ihm.listeners.ListenerParole;
import net.arkaine.easter.poker.ihm.listeners.ListenerPasser;
import net.arkaine.easter.poker.ihm.listeners.ListenerSuivre;
import net.arkaine.easter.poker.process.Partie;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class FenetrePrincipale extends JFrame {

	private static final long serialVersionUID = 5712929324731437363L;

	private static final FenetrePrincipale instance = new FenetrePrincipale();

	private ImageCarte carte1;
	private ImageCarte carte2;
	private ImageCarte flop1;
	private ImageCarte flop2;
	private ImageCarte flop3;
	private ImageCarte turn;
	private ImageCarte river;
	private ImageCarte carteAdversaire1;
	private ImageCarte carteAdversaire2;
	private ImageMise mise;
	private ImageMise miseAdversaire;
	private ImageMise pot;
	private JButton miser;
	private JButton passer;
	private JButton parole;
	private JButton suivre;
	private JSpinner sommeAMiser;
	private ImageMise sommeRestante;
	private ImageMise sommeAdverse;
	private JPanel dealer;



	private static final int MARGE=5;
	private static final int LARGEUR_CARTE=80;
	private static final int HAUTEUR_CARTE=100;


	private FenetrePrincipale(){
		super("Poker");
		try {
			setIconImage(ImageIO.read(getClass().getResourceAsStream("/img/jeton.png")));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel jeu = new PanelJeu();
		jeu.setLayout(null);
		carte1 = new ImageCarte(true);
		carte1.setBounds(500-LARGEUR_CARTE-MARGE/2, 490, LARGEUR_CARTE, HAUTEUR_CARTE);
		carte1.setOpaque(false);
		jeu.add(carte1);
		carte2=new ImageCarte(true);
		carte2.setBounds(500+MARGE/2, 490, LARGEUR_CARTE, HAUTEUR_CARTE);
		carte2.setOpaque(false);
		jeu.add(carte2);
		flop1=new ImageCarte(true);
		flop1.setBounds(290, 333-HAUTEUR_CARTE, LARGEUR_CARTE, HAUTEUR_CARTE);
		flop1.setOpaque(false);
		jeu.add(flop1);
		flop2=new ImageCarte(true);
		flop2.setBounds(290+LARGEUR_CARTE+MARGE, 333-HAUTEUR_CARTE, LARGEUR_CARTE, HAUTEUR_CARTE);
		flop2.setOpaque(false);
		jeu.add(flop2);
		flop3=new ImageCarte(true);
		flop3.setBounds(290+2*LARGEUR_CARTE+2*MARGE, 333-HAUTEUR_CARTE, LARGEUR_CARTE, HAUTEUR_CARTE);
		flop3.setOpaque(false);
		jeu.add(flop3);
		turn=new ImageCarte(true);
		turn.setBounds(290+3*LARGEUR_CARTE+3*MARGE, 333-HAUTEUR_CARTE, LARGEUR_CARTE, HAUTEUR_CARTE);
		turn.setOpaque(false);
		jeu.add(turn);
		river=new ImageCarte(true);
		river.setBounds(290+4*LARGEUR_CARTE+4*MARGE, 333-HAUTEUR_CARTE, LARGEUR_CARTE, HAUTEUR_CARTE);
		river.setOpaque(false);
		jeu.add(river);

		mise = new ImageMise();
		mise.setOpaque(false);
		mise.setBounds(510, 350, 100, 71);
		jeu.add(mise);

		miseAdversaire = new ImageMise();
		miseAdversaire.setOpaque(false);
		miseAdversaire.setBounds(430, 150, 100, 71);
		jeu.add(miseAdversaire);

		pot = new ImageMise(-1);
		pot.setOpaque(false);
		pot.setBounds(800, 250, 100, 71);
		jeu.add(pot);

		carteAdversaire1=new ImageCarte(false);
		carteAdversaire1.setBounds(500-LARGEUR_CARTE-MARGE/2, 30, LARGEUR_CARTE, HAUTEUR_CARTE);
		carteAdversaire1.setOpaque(false);
		jeu.add(carteAdversaire1);
		carteAdversaire2=new ImageCarte(false);
		carteAdversaire2.setOpaque(false);
		carteAdversaire2.setBounds(500+MARGE/2, 30, LARGEUR_CARTE, HAUTEUR_CARTE);
		jeu.add(carteAdversaire2);

		miser = new Bouton("Miser");
		miser.setBounds(40, 530, 90, 40);
		miser.addActionListener(ListenerMiser.getInstance());
		jeu.add(miser);
		passer = new Bouton("Passer");
		passer.setBounds(240, 530, 90, 40);
		passer.addActionListener(ListenerPasser.getInstance());
		jeu.add(passer);
		parole = new Bouton("Parole");
		parole.setBounds(140, 530, 90, 40);
		parole.addActionListener(ListenerParole.getInstance());
		jeu.add(parole);
		suivre = new Bouton("Suivre");
		suivre.setBounds(140, 580, 90, 40);
		suivre.addActionListener(ListenerSuivre.getInstance());
		jeu.add(suivre);

		sommeRestante = new ImageMise(10);
		sommeRestante.setBounds(590, 490, 100, 71);
		sommeRestante.setOpaque(false);
		jeu.add(sommeRestante);

		sommeAdverse =  new ImageMise(10);
		sommeAdverse.setOpaque(false);
		sommeAdverse.setBounds(310, 40, 100, 71);
		jeu.add(sommeAdverse);

		sommeAMiser = new JSpinner(new SpinnerNumberModel(2* Partie.BLIND_MIN, 2*Partie.BLIND_MIN, Partie.VALEUR_INITIALE, 10));
		sommeAMiser.setBounds(40, 490, 90, 30);
		sommeAMiser.setOpaque(false);
		jeu.add(sommeAMiser);

		dealer = new Dealer();
		setDealerJoueur();
		jeu.add(dealer);

		setLayout(new BorderLayout());
		JPanel config = PanelInfo.getInstance();
		config.setPreferredSize(new Dimension(200, 600));
		add(config, BorderLayout.EAST);
		add(jeu, BorderLayout.CENTER);
		setSize(1200, 666);
		setLocationRelativeTo(null);

		setBoutonsActif(false);
		setVisible(true);
	}

	public void setSommeRestante(int valeur){
		sommeRestante.setValeurMise(valeur);
	}

	public void setSommeAdverse(int valeur){
		sommeAdverse.setValeurMise(valeur);
	}

	public void setDealerJoueur(){
		dealer.setBounds(620, 380, 100, 100);
		repaint();
	}

	public void setDealerAdversaire(){
		dealer.setBounds(350, 150, 100, 100);
		repaint();
	}

	public void updateSpinner(){
		if(getSommeRestante()<20){
			sommeAMiser.setModel(new SpinnerNumberModel(getSommeRestante(), getSommeRestante(), getSommeRestante(), 10));			
		}
		else {
			sommeAMiser.setModel(new SpinnerNumberModel(Math.max(2*Partie.BLIND_MIN, getMiseAdversaire().getValeurMise()-getMise().getValeurMise()), Math.max(2*Partie.BLIND_MIN, getMiseAdversaire().getValeurMise()-getMise().getValeurMise()), getSommeRestante(), 10));
		}
	}

	public void setBoutonsActif(boolean etat){
		miser.setEnabled(etat);
		updateSpinner();
		if(getMise().getValeurMise()<getMiseAdversaire().getValeurMise()){
			parole.setEnabled(false);
		}
		else {
			parole.setEnabled(etat);
		}
		passer.setEnabled(etat);
		suivre.setEnabled(etat);
		sommeAMiser.setEnabled(etat);
	}

	public static FenetrePrincipale getInstance() {
		return instance;
	}

	/**
	 * @return the carte1
	 */
	public ImageCarte getCarte1() {
		return carte1;
	}

	/**
	 * @return the carte2
	 */
	public ImageCarte getCarte2() {
		return carte2;
	}

	/**
	 * @return the flop1
	 */
	public ImageCarte getFlop1() {
		return flop1;
	}

	/**
	 * @return the flop2
	 */
	public ImageCarte getFlop2() {
		return flop2;
	}

	/**
	 * @return the flop3
	 */
	public ImageCarte getFlop3() {
		return flop3;
	}

	/**
	 * @return the turn
	 */
	public ImageCarte getTurn() {
		return turn;
	}

	/**
	 * @return the river
	 */
	public ImageCarte getRiver() {
		return river;
	}

	/**
	 * @return the carteAdversaire1
	 */
	public ImageCarte getCarteAdversaire1() {
		return carteAdversaire1;
	}

	/**
	 * @return the carteAdversaire2
	 */
	public ImageCarte getCarteAdversaire2() {
		return carteAdversaire2;
	}

	/**
	 * @return the mise
	 */
	public ImageMise getMise() {
		return mise;
	}

	/**
	 * @return the miseAdversaire
	 */
	public ImageMise getMiseAdversaire() {
		return miseAdversaire;
	}

	/**
	 * @return the pot
	 */
	public ImageMise getPot() {
		return pot;
	}

	/**
	 * @return the miser
	 */
	public JButton getMiser() {
		return miser;
	}

	/**
	 * @return the passer
	 */
	public JButton getPasser() {
		return passer;
	}

	/**
	 * @return the parole
	 */
	public JButton getParole() {
		return parole;
	}

	/**
	 * @return the sommeAMiser
	 */
	public JSpinner getSommeAMiser() {
		return sommeAMiser;
	}

	/**
	 * @return the sommeRestante
	 */
	public int getSommeRestante() {
		return sommeRestante.getValeurMise();
	}

	/**
	 * @return the sommeAdverse
	 */
	public int getSommeAdverse() {
		return sommeAdverse.getValeurMise();
	}

}
