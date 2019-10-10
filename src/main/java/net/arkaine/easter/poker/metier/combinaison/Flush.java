package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Comparateur;
import net.arkaine.easter.poker.metier.Couleur;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flush extends AbstractCombinaison {

	private String couleur;

	public Flush() {
		super("Couleur");
	}

	@Override
	public boolean verifier(List<Carte> cartes) {
		boolean res=false;
		if(cartes.size()>4){
			int coeur=0;
			int pique=0;
			int carreau=0;
			int treffle=0;
			for(Carte c : cartes){
				if(c.getCouleur().equals(Couleur.COEUR)){
					coeur++;
				}
				else if(c.getCouleur().equals(Couleur.PIQUE)){
					pique++;
				}
				else if(c.getCouleur().equals(Couleur.TREFLE)){
					treffle++;	
				}
				else {
					carreau++;				
				}
			}
			Collections.sort(cartes);
			res=carreau>4 || coeur > 4 || pique > 4 || treffle > 4;
			setCombinaisonProche(carreau>3 || coeur > 3 || pique > 3 || treffle > 3);
			if(res){
				List<Carte> cartesCouleur;
				if(carreau>4){
					couleur="carreau";
					cartesCouleur=conserverCouleur(cartes, Couleur.CARREAU);
				}
				else if(coeur>4){
					couleur="coeur";
					cartesCouleur=conserverCouleur(cartes, Couleur.COEUR);
				}
				else if(pique>4){
					couleur="pique";
					cartesCouleur=conserverCouleur(cartes, Couleur.PIQUE);
				}
				else {
					cartesCouleur=conserverCouleur(cartes, Couleur.TREFLE);
					couleur="trèfle";
				}
				Collections.sort(cartesCouleur, new Comparateur());
				List<Valeur> kikers = new ArrayList<Valeur>();
				for(int i=1; i<=5; i++){
					kikers.add(cartesCouleur.get(cartesCouleur.size()-i).getValeur());
				}
				setKikers(kikers);
			}
		}
		return res;
	}

	@Override
	public String getDescription() {
		return "Couleur  "+couleur;
	}

	private List<Carte> conserverCouleur(List<Carte> cartes, Couleur couleur){
		List<Carte> res = new ArrayList<Carte>();
		for(Carte c : cartes){
			if(c.getCouleur().equals(couleur)){
				res.add(c);
			}
		}
		return res;
	}

	@Override
	public int getValeur() {
		return 5;
	}

}
