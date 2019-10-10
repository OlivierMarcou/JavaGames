package net.arkaine.easter.poker.metier;

public class Carte implements Comparable<Carte> {
	
	private Couleur couleur;
	private Valeur valeur;
	
	/**
	 * @param couleur
	 * @param valeur
	 */
	public Carte(Couleur couleur, Valeur valeur) {
		this.couleur = couleur;
		this.valeur = valeur;
	}

	@Override
	public int compareTo(Carte o) {
		return valeur.compareTo(o.getValeur())+couleur.compareTo(o.getCouleur());
	}

	/**
	 * @return the couleur
	 */
	public Couleur getCouleur() {
		return couleur;
	}

	/**
	 * @return the valeur
	 */
	public Valeur getValeur() {
		return valeur;
	}
	
	@Override
	public String toString(){
		return valeur.toString()+" de "+couleur.toString();
	}

}
