package net.arkaine.easter.poker.metier;

public class Valeur implements Comparable<Valeur> {

	public static Valeur DEUX = new Valeur("Deux", 2);
	public static Valeur TROIS = new Valeur("Trois", 3);
	public static Valeur QUATRE = new Valeur("Quatre", 4);
	public static Valeur CINQ = new Valeur("Cinq", 5);
	public static Valeur SIX = new Valeur("Six", 6);
	public static Valeur SEPT = new Valeur("Sept", 7);
	public static Valeur HUIT = new Valeur("Huit", 8);
	public static Valeur NEUF = new Valeur("Neuf", 9);
	public static Valeur DIX = new Valeur("Dix", 10);
	public static Valeur VALET = new Valeur("Valet", 11);
	public static Valeur DAME = new Valeur("Dame", 12);
	public static Valeur ROI = new Valeur("Roi", 13);
	public static Valeur AS = new Valeur("As", 14);

	private String nom;
	private int position;

	private Valeur(String nom, int position){
		this.nom=nom;
		this.position=position;
	}

	@Override
	public int compareTo(Valeur o) {
		if(this.equals(AS) && o.equals(DEUX)){
			return -1;
		}
		else if(o.equals(AS) && this.equals(DEUX)){
			return 1;
		}
		return position-o.getPosition();
	}

	public int getPosition() {
		return position;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	
	@Override
	public String toString(){
		return getNom();
	}

	public static Valeur[] getAllValeurs(){
		return new Valeur[]{DEUX, TROIS, QUATRE, CINQ, SIX, SEPT, HUIT, NEUF, DIX, VALET, DAME, ROI, AS};
	}



}
