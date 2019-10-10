package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.List;

public class Full extends AbstractCombinaison {

	public Full() {
		super("Full");
	}

	@Override
	public boolean verifier(List<Carte> cartes) {
		boolean res=false;
		if(cartes.size()>4){
			for(Valeur valeur1 : Valeur.getAllValeurs()){
				for(Valeur valeur2 : Valeur.getAllValeurs()){
					if(valeur1.compareTo(valeur2)>0){
						int cpt1=compterValeur(valeur1, cartes);
						int cpt2=compterValeur(valeur2, cartes);;
						if(cpt1==3 && cpt2==2){
							setValeurHaute(valeur1);
							setValeurBasse(valeur2);
							res=true;
						}
						else if(cpt1==2 && cpt2==3){
							setValeurHaute(valeur2);
							setValeurBasse(valeur1);
							res=true;
						}
					}
				}
			}
		}
		return res;
	}

	@Override
	public int getValeur() {
		return 6;
	}

	@Override
	public String getDescription() {
		return "Full aux "+getValeurHaute().getNom().toLowerCase()+" par les "+getValeurBasse().getNom().toLowerCase();
	}


}
