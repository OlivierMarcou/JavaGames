package net.arkaine.easter.poker.metier;

public class Couleur implements Comparable<Couleur> {
	
	public static Couleur TREFLE = new Couleur("Trefle");
	public static Couleur PIQUE = new Couleur("Pique");
	public static Couleur COEUR = new Couleur("Coeur");
	public static Couleur CARREAU = new Couleur("Carreau");
	
	private String nom;
	
	private Couleur(String nom){
		this.nom=nom;
	}

	@Override
	public int compareTo(Couleur o) {
		if(nom.compareTo(o.getNom())==0){
			return 0;
		}
		else {
			return 100;
		}
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	
	public static Couleur[] getAllCouleurs(){
		return new Couleur[]{PIQUE, COEUR, TREFLE, CARREAU};
	}
	
	@Override
	public String toString(){
		return getNom();
	}

}
