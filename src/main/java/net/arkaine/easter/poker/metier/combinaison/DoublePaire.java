package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Comparateur;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoublePaire extends AbstractCombinaison {

	public DoublePaire() {
		super("Double paire");
	}

	@Override
	public boolean verifier(List<Carte> cartes) {
		boolean res=false;
		setCombinaisonProche(false);
		if(cartes.size()>3){
			for(Valeur valeur1 : Valeur.getAllValeurs()){
				for(Valeur valeur2 : Valeur.getAllValeurs()){
					if(valeur1.compareTo(valeur2)>0){
						int cpt1=compterValeur(valeur1, cartes);
						int cpt2=compterValeur(valeur2, cartes);;
						if(cpt1==2 && cpt2==2){
							setValeurHaute(valeur1);
							setValeurBasse(valeur2);
							List<Valeur> kikers = new ArrayList<Valeur>();
							Collections.sort(cartes, new Comparateur());
							int size=cartes.size();
							int i=1;
							while(kikers.size()<1 && i<=size){
								if(!cartes.get(size-i).getValeur().equals(valeur1) && !cartes.get(size-i).getValeur().equals(valeur2)){
									kikers.add(cartes.get(size-i).getValeur());
								}
								i++;
							}
							setKikers(kikers);
							res=true;
						}
						if(cpt1==2 || cpt2==2){
							setCombinaisonProche(true);
						}
					}
				}
			}
		}
		return res;
	}

	@Override
	public String getDescription() {
		return "Double paire : "+getValeurHaute().getNom().toLowerCase()+" et "+getValeurBasse().getNom().toLowerCase();
	}

	@Override
	public int getValeur() {
		return 2;
	}

}
