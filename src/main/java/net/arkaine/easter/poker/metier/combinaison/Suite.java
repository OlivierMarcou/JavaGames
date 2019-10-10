package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.*;

public class Suite extends AbstractCombinaison {

	public Suite() {
		super("Suite");
	}
	@Override
	public boolean verifier(List<Carte> cartes) {
		int cpt=0;
		if(cartes.size()>4){
			Set<Valeur> svaleurs = new HashSet<Valeur>();
			for(Carte c : cartes){
				svaleurs.add(c.getValeur());
			}
			List<Valeur> valeurs = new ArrayList<Valeur>(svaleurs) ;
			Collections.sort(valeurs);
			if(valeurs.get(valeurs.size()-1).equals(Valeur.AS)){
				valeurs.add(0, valeurs.get(valeurs.size()-1));
			}
			List<Integer> differences = new ArrayList<Integer>();
			for (int i=1; i<valeurs.size(); i++){
				if(valeurs.get(i-1).equals(Valeur.AS) && valeurs.get(i).equals(Valeur.DEUX)){
					differences.add(1);
				}
				else {
					if(valeurs.get(i).compareTo(valeurs.get(i-1))!=0){
						differences.add(valeurs.get(i).compareTo(valeurs.get(i-1)));
					}
				}
			}
			cpt=0;
			for(int i : differences){
				if(i==1){
					cpt++;
					setValeurHaute(valeurs.get(i));
				}
				else {
					if(cpt==4){
						return true;
					}
					cpt=0;
				}
			}
		}
		setCombinaisonProche(cpt==3);
		return cpt==4;
	}

	@Override
	public int getValeur() {
		return 4;
	}

	@Override
	public String getDescription() {
		return "Suite";
	}



}
