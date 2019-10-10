package net.arkaine.easter.poker.metier.combinaison;

import net.arkaine.easter.poker.metier.Carte;
import net.arkaine.easter.poker.metier.Valeur;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCombinaison implements Comparable<AbstractCombinaison> {


	private String nom;
	private Valeur valeurHaute;
	private Valeur valeurBasse;
	private List<Valeur> kikers;
	private boolean combinaisonProche;

	protected AbstractCombinaison(String nom){
		this.nom=nom;
	}

	public abstract boolean verifier(List<Carte> cartes);
	
	public abstract String getDescription();

	public abstract int getValeur();

	public int compterValeur(Valeur v, List<Carte> cartes){
		int cpt=0;
		for(Carte c : cartes){
			if(c.getValeur().equals(v)){
				cpt++;
			}
		}
		return cpt;
	}

	public int comparerKikers(AbstractCombinaison c){
		int res=0;
		if(getKikers()!=null){
			Collections.sort(getKikers());
			Collections.sort(c.getKikers());
			int i=1;
			while(i<=getKikers().size() && res==0){
				res=getKikers().get(getKikers().size()-i).compareTo(c.getKikers().get(getKikers().size()-i));
				i++;
			}
		}
		return res;
	}

	@Override
	public int compareTo(AbstractCombinaison c){
		int res=getValeur()-c.getValeur();
		if(res==0){
			if(this instanceof Paire || this instanceof Brelan || this instanceof Carre || this instanceof Suite || this instanceof QuinteFlush){
				res=getValeurHaute().compareTo(c.getValeurHaute());
			}
			else if(this instanceof Full || this instanceof DoublePaire){
				res=10*(getValeurHaute().compareTo(c.getValeurHaute()))+getValeurBasse().compareTo(c.getValeurBasse());
			}
			if(res==0){
				res=comparerKikers(c);
			}
		}
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractCombinaison)) {
			return false;
		}
		AbstractCombinaison other = (AbstractCombinaison) obj;
		if (nom == null) {
			if (other.nom != null) {
				return false;
			}
		}
		else if (!nom.equals(other.nom)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the kikers
	 */
	public List<Valeur> getKikers() {
		return kikers;
	}

	/**
	 * @param kikers the kikers to set
	 */
	public void setKikers(List<Valeur> kikers) {
		this.kikers = kikers;
	}
	
	/**
	 * @return the combinaisonProche
	 */
	public boolean isCombinaisonProche() {
		return combinaisonProche;
	}

	/**
	 * @param combinaisonProche the combinaisonProche to set
	 */
	public void setCombinaisonProche(boolean combinaisonProche) {
		this.combinaisonProche = combinaisonProche;
	}
	
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @return the valeurHaute
	 */
	public Valeur getValeurHaute() {
		return valeurHaute;
	}

	/**
	 * @param valeurHaute the valeurHaute to set
	 */
	public void setValeurHaute(Valeur valeurHaute) {
		this.valeurHaute = valeurHaute;
	}

	/**
	 * @return the valeurBasse
	 */
	public Valeur getValeurBasse() {
		return valeurBasse;
	}

	/**
	 * @param valeurBasse the valeurBasse to set
	 */
	public void setValeurBasse(Valeur valeurBasse) {
		this.valeurBasse = valeurBasse;
	}

}
