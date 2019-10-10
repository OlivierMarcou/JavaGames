package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Comparateur;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rien extends AbstractCombinaison {

	public Rien() {
		super("Carte haute");
	}

	@Override
	public int getValeur() {
		return 0;
	}

	@Override
	public boolean verifier(List<Carte> cartes) {
		setCombinaisonProche(true);
		if(cartes.size()>0){
			Collections.sort(cartes, new Comparateur());
			List<Valeur> kikers = new ArrayList<Valeur>();
			int size=cartes.size();
			int i=1;
			while(size-i>=0 && kikers.size()<5){
				kikers.add(cartes.get(size-i).getValeur());
				i++;
			}
			setKikers(kikers);
		}
		return true;
	}

	@Override
	public String getDescription() {
		return "Aucune combinaison";
	}


}
