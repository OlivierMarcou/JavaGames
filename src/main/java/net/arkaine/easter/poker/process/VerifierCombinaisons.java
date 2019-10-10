package net.arkaine.easter.poker.process;

import net.arkaine.easter.poker.ihm.FenetrePrincipale;
import net.arkaine.easter.poker.lancement.Info;
import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Couleur;
import net.arkaine.easter.poker.metier.Valeur;
import net.arkaine.easter.poker.metier.combinaison.*;

import java.util.ArrayList;
import java.util.List;

public class VerifierCombinaisons {

	private static final VerifierCombinaisons instance = new VerifierCombinaisons();

	private AbstractCombinaison combinaisonProbable;

	public VerifierCombinaisons(){
		super();
	}

	public int isJoueurGagne(){
		Carte[] cartesJoueur = new Carte[]{
				FenetrePrincipale.getInstance().getFlop1().getCarte(), 
				FenetrePrincipale.getInstance().getFlop2().getCarte(),
				FenetrePrincipale.getInstance().getFlop3().getCarte(), 
				FenetrePrincipale.getInstance().getTurn().getCarte(), 
				FenetrePrincipale.getInstance().getRiver().getCarte(),
				FenetrePrincipale.getInstance().getCarte1().getCarte(),
				FenetrePrincipale.getInstance().getCarte2().getCarte()
		};
		Carte[] cartesAdversaire = new Carte[]{
				FenetrePrincipale.getInstance().getFlop1().getCarte(), 
				FenetrePrincipale.getInstance().getFlop2().getCarte(), 
				FenetrePrincipale.getInstance().getFlop3().getCarte(), 
				FenetrePrincipale.getInstance().getTurn().getCarte(), 
				FenetrePrincipale.getInstance().getRiver().getCarte(),
				FenetrePrincipale.getInstance().getCarteAdversaire1().getCarte(),
				FenetrePrincipale.getInstance().getCarteAdversaire2().getCarte()
		};
		AbstractCombinaison combinaisonJoueur = deteminerCombinaison(cartesJoueur);
		Info.addTexte("Vous avez : "+combinaisonJoueur.getDescription());
		AbstractCombinaison combinaisonAdversaire = deteminerCombinaison(cartesAdversaire);
		Info.addTexte("Votre adversaire a : "+combinaisonAdversaire.getDescription());
		return combinaisonJoueur.compareTo(combinaisonAdversaire);
	}

	public AbstractCombinaison deteminerCombinaison(Carte[] cartes){
		List<Carte> cartesJoueur = new ArrayList<Carte>();
		for(Carte c : cartes){
			if(c!=null){
				cartesJoueur.add(c);
			}
		}
		AbstractCombinaison combinaisonJoueur=null;
		QuinteFlush qfJoueur = new QuinteFlush();
		if(qfJoueur.verifier(cartesJoueur)){
			combinaisonJoueur=qfJoueur;
		}
		else {
			Carre carreJoueur = new Carre();
			if(carreJoueur.verifier(cartesJoueur)){
				combinaisonJoueur=carreJoueur;
			}
			else {
				Full fullJoueur = new Full();
				if(fullJoueur.verifier(cartesJoueur)){
					combinaisonJoueur=fullJoueur;
				}
				else {
					Flush couleurJoueur = new Flush();
					if(couleurJoueur.verifier(cartesJoueur)){
						combinaisonJoueur=couleurJoueur;
					}
					else {
						Suite suiteJoueur = new Suite();
						if(suiteJoueur.verifier(cartesJoueur)){
							combinaisonJoueur=suiteJoueur;
						}
						else {
							Brelan brelanJoueur = new Brelan();
							if(brelanJoueur.verifier(cartesJoueur)){
								combinaisonJoueur=brelanJoueur;
							}
							else {
								DoublePaire dpJoueur = new DoublePaire();
								if(dpJoueur.verifier(cartesJoueur)){
									combinaisonJoueur=dpJoueur;
								}
								else {
									Paire paireJoueur = new Paire();
									if(paireJoueur.verifier(cartesJoueur)){
										combinaisonJoueur=paireJoueur;
									}
									else {
										Rien rienJoueur = new Rien();
										rienJoueur.verifier(cartesJoueur);
										combinaisonJoueur=rienJoueur;
									}
									if(paireJoueur.isCombinaisonProche()){
										combinaisonProbable=paireJoueur;
									}
								}
								if(dpJoueur.isCombinaisonProche()){
									combinaisonProbable=dpJoueur;
								}
							}
							if(brelanJoueur.isCombinaisonProche()){
								combinaisonProbable=brelanJoueur;
							}
						}
						if(suiteJoueur.isCombinaisonProche()){
							combinaisonProbable=suiteJoueur;
						}
					}
					if(couleurJoueur.isCombinaisonProche()){
						combinaisonProbable=couleurJoueur;
					}
				}
				if(fullJoueur.isCombinaisonProche()){
					combinaisonProbable=fullJoueur;
				}
			}
			if(carreJoueur.isCombinaisonProche()){
				combinaisonProbable=carreJoueur;
			}
		}
		if(qfJoueur.isCombinaisonProche()){
			combinaisonProbable=qfJoueur;
		}
		return combinaisonJoueur;
	}

	public static void main(String[] args){
		List<Carte> cartes = new ArrayList<Carte>();
		cartes.add(new Carte(Couleur.CARREAU, Valeur.DEUX));
		cartes.add(new Carte(Couleur.TREFLE, Valeur.TROIS));
		cartes.add(new Carte(Couleur.COEUR, Valeur.QUATRE));
		cartes.add(new Carte(Couleur.CARREAU, Valeur.QUATRE));
		cartes.add(new Carte(Couleur.PIQUE, Valeur.CINQ));
		cartes.add(new Carte(Couleur.COEUR, Valeur.NEUF));
		cartes.add(new Carte(Couleur.PIQUE, Valeur.AS));
	}
	
	/**
	 * @return the combinaisonProbable
	 */
	public AbstractCombinaison getCombinaisonProbable() {
		return combinaisonProbable;
	}

	/**
	 * @return the instance
	 */
	public static VerifierCombinaisons getInstance() {
		return instance;
	}


}
