package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Comparateur;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Paire extends AbstractCombinaison {

	public Paire() {
		super("Paire");
	}

	@Override
	public boolean verifier(List<Carte> cartes) {
		boolean res=false;
		setCombinaisonProche(true);
		if(cartes.size()>1){
			for(Valeur v : Valeur.getAllValeurs()){
				if(compterValeur(v, cartes)==2){
					setValeurHaute(v);
					List<Valeur> kikers = new ArrayList<Valeur>();
					Collections.sort(cartes, new Comparateur());
					int size=cartes.size();
					int i=1;
					while(kikers.size()<3 && i<=size){
						if(size-i>=0 && !cartes.get(size-i).getValeur().equals(v)){
							kikers.add(cartes.get(size-i).getValeur());
						}
						i++;
					}
					setKikers(kikers);
					res=true;
				}
			}
		}
		return res;
	}

	@Override
	public int getValeur() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "Paire de "+getValeurHaute().getNom().toLowerCase();
	}

}
