package net.arkaine.easter.poker.process;

import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.ihm.ImageCarte;
import net.arkaine.easter.poker.ihm.PanelInfo;
import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Paquet;

import javax.swing.*;

public class Partie extends Thread {

	private static final Partie instance = new Partie();

	public static boolean DEAL_JOUEUR=true;
	public static int CARTE_COURANTE=0;
	public static int VALEUR_INITIALE=1500;
	public static int BLIND_MIN=10;
	public static boolean PASSER=false;
	public static boolean IA_PASSER=false;
	private Paquet paquet = Paquet.getInstance();
	private OutilsThread outils = OutilsThread.getInstance();
	private boolean encours=true;


	@Override
	public void run(){
		final FenetrePrincipale fp = FenetrePrincipale.getInstance();
		JOptionPane.showMessageDialog(null, "La partie va commencer");
		fp.setSommeRestante(VALEUR_INITIALE);
		fp.setSommeAdverse(VALEUR_INITIALE);
		int nbTours=1;
		int nbVicoire=0;
		int nbDefaites=0;
		int nbPasse=0;
		while (encours) {
			PanelInfo.getInstance().afficherStat(nbTours-1, nbVicoire, nbDefaites, nbPasse);
			PanelInfo.getInstance().afficherCombinaison(null);
			paquet.melanger();
			Info.addTitre("* Tour "+nbTours++);
			CARTE_COURANTE=0;
			if(DEAL_JOUEUR){
				miser(BLIND_MIN);
				outils.attendre();
				miseAdverse(2*BLIND_MIN);
				outils.attendre();
				continuerTour(false, 0, new ImageCarte[]{fp.getCarteAdversaire1(), fp.getCarte1(), fp.getCarteAdversaire2(), fp.getCarte2()});
			}
			else {
				miseAdverse(BLIND_MIN);
				outils.attendre();
				miser(2*BLIND_MIN);
				outils.attendre();
				continuerTour(true, 0, new ImageCarte[]{fp.getCarte1(), fp.getCarteAdversaire1(), fp.getCarte2(), fp.getCarteAdversaire2()});
			}
			if(!PASSER && !IA_PASSER){
				jouerTour(DEAL_JOUEUR, 1, new ImageCarte[]{fp.getFlop1(), fp.getFlop2(), fp.getFlop3()});
				if(!PASSER && !IA_PASSER){
					jouerTour(DEAL_JOUEUR, 2, new ImageCarte[]{fp.getTurn()});
					if(!PASSER && !IA_PASSER){
						jouerTour(DEAL_JOUEUR, 3, new ImageCarte[]{fp.getRiver()});
					}
				}
			}		
			if(PASSER){
				fp.getCarte1().setAfficher(false);
				fp.getCarte2().setAfficher(false);
				outils.attendre();
				nbPasse++;
			}
			else if (!IA_PASSER){
				fp.getCarteAdversaire1().setAfficher(true);
				fp.getCarteAdversaire2().setAfficher(true);
				outils.attendre();
			}
			int joueurGagne=0;
			if(!IA_PASSER && !PASSER){
				joueurGagne=VerifierCombinaisons.getInstance().isJoueurGagne();
			}
			if(IA_PASSER || joueurGagne>0){
				nbVicoire++;
				JOptionPane.showMessageDialog(fp, "Vous gagnez");
				fp.setSommeRestante(fp.getSommeRestante()+fp.getPot().getValeurMise()+fp.getMise().getValeurMise()+fp.getMiseAdversaire().getValeurMise());
			}
			else if(PASSER || joueurGagne<0){
				nbDefaites++;
				JOptionPane.showMessageDialog(fp, "Votre adversaire gagne");
				fp.setSommeAdverse(fp.getSommeAdverse()+fp.getPot().getValeurMise()+fp.getMise().getValeurMise()+fp.getMiseAdversaire().getValeurMise());
			}
			else {
				JOptionPane.showMessageDialog(fp, "Egalite");
				final int aPartager=fp.getPot().getValeurMise()+fp.getMise().getValeurMise()+fp.getMiseAdversaire().getValeurMise();
				fp.setSommeRestante(fp.getSommeRestante()+aPartager/2);
				fp.setSommeAdverse(fp.getSommeAdverse()+aPartager/2);
			}
			fp.getPot().setValeurMise(-1);
			fp.getMise().setValeurMise(-1);
			fp.getMiseAdversaire().setValeurMise(-1);
			for(ImageCarte ic : new ImageCarte[]{fp.getFlop1(), fp.getFlop2(), fp.getFlop3(), fp.getTurn(), fp.getRiver(), fp.getCarte1(), fp.getCarte2(), fp.getCarteAdversaire1(), fp.getCarteAdversaire2()}){
				ic.setCarte(null);
			}
			fp.getCarteAdversaire1().setAfficher(false);
			fp.getCarteAdversaire2().setAfficher(false);
			fp.getCarte1().setAfficher(true);
			fp.getCarte2().setAfficher(true);
			tourSuivant();
		}
	}

	private void tourSuivant(){
		if(FenetrePrincipale.getInstance().getSommeAdverse()==0){
			JOptionPane.showMessageDialog(null, "Vous avez gagné !");
			encours=false;
		}
		if(FenetrePrincipale.getInstance().getSommeRestante()==0){
			JOptionPane.showMessageDialog(null, "Vous avez perdu !");
			encours=false;
		}
		DEAL_JOUEUR=!DEAL_JOUEUR;
		PASSER=false;
		IA_PASSER=false;
		if(DEAL_JOUEUR){
			FenetrePrincipale.getInstance().setDealerJoueur();
		}
		else {
			FenetrePrincipale.getInstance().setDealerAdversaire();
		}
	}
	private void donnerCartes(ImageCarte[] cartes) {
		for(final ImageCarte ic : cartes){
			ic.setCarte(Paquet.getInstance().get(Partie.CARTE_COURANTE++));
			OutilsThread.getInstance().attendre();
		}
		Carte[] cartesJoueur = new Carte[]{
				FenetrePrincipale.getInstance().getFlop1().getCarte(), 
				FenetrePrincipale.getInstance().getFlop2().getCarte(), 
				FenetrePrincipale.getInstance().getFlop3().getCarte(), 
				FenetrePrincipale.getInstance().getTurn().getCarte(), 
				FenetrePrincipale.getInstance().getRiver().getCarte(),
				FenetrePrincipale.getInstance().getCarte1().getCarte(),
				FenetrePrincipale.getInstance().getCarte2().getCarte()
		};
		PanelInfo.getInstance().afficherCombinaison(VerifierCombinaisons.getInstance().deteminerCombinaison(cartesJoueur));
	}

	private void jouerTour(boolean dealJoueur, int tour, ImageCarte[] cartesADonner) {
		FenetrePrincipale.getInstance().getPot().setValeurMise(FenetrePrincipale.getInstance().getPot().getValeurMise()+FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise()+FenetrePrincipale.getInstance().getMise().getValeurMise());
		FenetrePrincipale.getInstance().getMise().setValeurMise(-1);
		FenetrePrincipale.getInstance().getMiseAdversaire().setValeurMise(-1);
		continuerTour(dealJoueur, tour, cartesADonner);
	}

	private void continuerTour(boolean dealJoueur, int tour, ImageCarte[] cartesADonner) {
		boolean tour1=true;
		boolean tourJoueur=!dealJoueur;
		donnerCartes(cartesADonner);
		outils.attendre();
		do {
			if(!tourJoueur){
				IA.getInstance().jouer(tour);
			}
			else {
				demandeJeuUtilisateur();
			}
			tourJoueur=!tourJoueur;
			outils.attendre();
			if(tour1 || (FenetrePrincipale.getInstance().getMise().getValeurMise()!=FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise())){
				tour1=false;
				if(tourJoueur && !IA_PASSER){
					demandeJeuUtilisateur();
				}
				if(!tourJoueur && !PASSER){
					IA.getInstance().jouer(tour);
				}
				tourJoueur=!tourJoueur;
				outils.attendre();
			}
		} while(FenetrePrincipale.getInstance().getMise().getValeurMise()!=FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise() && !PASSER && !IA_PASSER);
	}

	private void demandeJeuUtilisateur(){
		if(FenetrePrincipale.getInstance().getSommeRestante()==0 || FenetrePrincipale.getInstance().getSommeAdverse()==0){
			Partie.getInstance().suivre();
		}
		else {
			FenetrePrincipale.getInstance().setBoutonsActif(true);
			outils.attendreOperationUtilisateur();
			FenetrePrincipale.getInstance().setBoutonsActif(false);
		}
	}

	public void miser(final int valeur) {
		final int total=valeur+FenetrePrincipale.getInstance().getMise().getValeurMise();
		FenetrePrincipale.getInstance().getMise().setValeurMise(total);
		FenetrePrincipale.getInstance().setSommeRestante(FenetrePrincipale.getInstance().getSommeRestante()-valeur);
	}

	public void miseAdverse(final int valeur) {
		final int total=valeur+FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise();
		FenetrePrincipale.getInstance().getMiseAdversaire().setValeurMise(total);
		FenetrePrincipale.getInstance().setSommeAdverse(FenetrePrincipale.getInstance().getSommeAdverse()-valeur);
		FenetrePrincipale.getInstance().updateSpinner();
	}

	public void suivre() {
		miser(FenetrePrincipale.getInstance().getMiseAdversaire().getValeurMise()-FenetrePrincipale.getInstance().getMise().getValeurMise());
	}

	public static Partie getInstance() {
		return instance;
	}

}
