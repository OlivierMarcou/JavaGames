package net.arkaine.easter.poker.metier;
import java.util.ArrayList;
import java.util.Collections;


public class Paquet extends ArrayList<Carte> {
	
	private static final long serialVersionUID = -3776643065999104519L;

	private static final Paquet instance = new Paquet();
	
	private Paquet(){
		for(Couleur c : Couleur.getAllCouleurs()){
			for(Valeur v : Valeur.getAllValeurs()){
				add(new Carte(c, v));
			}
		}
	}

	public void melanger(){
		Collections.shuffle(this);
	}
	
	public void trier(){
		Collections.sort(this);
	}
	
	public Carte piocher(){
		return remove(0);
	}
	
	public static Paquet getInstance() {
		return instance;
	}
	
	

}
